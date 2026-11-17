Configured authentication: Organization

Advice-only request, so I'll survey the relevant code without modifying anything.

I read `class-work.md`, `AGENTS.md`, both entities, both DAOs, `DatabaseModule`, `SkyWatchDatabase`, your existing `SearchViewModel`, and the `core` service layer. This is advice only; I haven't changed any files.
## Summary

Use three layers: DAO → repository → service. The repositories are plain Kotlin and use coroutines internally. The services are the public API that viewmodels see. Their methods return only `LiveData` (for data you watch) and `CompletableFuture` (for one-off actions), so Java and Kotlin viewmodels can both use them.

You already do this in `core`: `MediaSearchServiceImpl` uses coroutines inside but returns `CompletableFuture` from `MediaSearchService`. Following the same pattern keeps the project consistent.

## 1. Repositories (Kotlin, used only by services)

Each repository is a thin wrapper around its DAO. It knows nothing about who the current user is.

```kotlin
@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {
    fun get(id: Long): LiveData<User> = userDao.select(id)
    suspend fun getOrCreate(oauthKey: String, displayName: String, avatar: URL?): User =
        userDao.select(oauthKey)
            ?: User(oauthKey = oauthKey, displayName = displayName, avatar = avatar, created = Clock.System.now())
                .let { it.copy(id = userDao.insert(it)) }
    suspend fun update(user: User) = userDao.update(user)
    suspend fun delete(user: User) = userDao.delete(user)
}

@Singleton
class FavoriteRepository @Inject constructor(private val favoriteDao: FavoriteDao) {
    fun getAll(userId: Long, order: FavoriteOrder): LiveData<List<Favorite>> = when (order) {
        FavoriteOrder.DESCRIPTION -> favoriteDao.selectOrderByDescription(userId)
        FavoriteOrder.FAVORITED -> favoriteDao.selectOrderByFavorited(userId)
    }
    suspend fun add(favorite: Favorite): Favorite = favorite.copy(id = favoriteDao.insert(favorite))
    suspend fun remove(favorite: Favorite) = favoriteDao.delete(favorite)
}
```
`suspend` is fine here because only Kotlin services call these methods. An enum like `FavoriteOrder` is easier for callers than choosing between two DAO methods.

## 2. Service layer: yes, it's worth having

There are two reasons:

1. **The current user is shared state.** One app-wide object should own it. Then every viewmodel and service sees the same user, and favorites change automatically when the user changes.
2. **Java support.** The service is where you turn `suspend` functions into something Java can call.

```kotlin
interface UserService {
    val currentUser: LiveData<User?>
    fun signIn(oauthKey: String, displayName: String, avatar: URL?): CompletableFuture<User>
    fun signOut()
}

interface FavoriteService {
    @JvmOverloads
    fun getFavorites(order: FavoriteOrder = FavoriteOrder.FAVORITED): LiveData<List<Favorite>>
    fun add(image: SearchResultImage): CompletableFuture<Favorite>
    fun remove(favorite: Favorite): CompletableFuture<Void?>   // Void works better in Java than Unit
}
```

How the implementations work:
- **`UserServiceImpl`** keeps a private `MutableLiveData<User?>` (or a `MutableStateFlow` exposed through `.asLiveData()`). `signIn` runs `scope.future { repository.getOrCreate(...) }` and posts the result.
- **`FavoriteServiceImpl`** gets `UserService` injected. `getFavorites` returns `currentUser.switchMap { user -> user?.let { repository.getAll(it.id, order) } ?: MutableLiveData(emptyList()) }`, so the favorites list updates by itself when the user changes. `add` reads the current user's ID, so viewmodels never pass a user ID in.
- **Bind with Hilt** through a `ServiceModule` in `app` using `@Binds @Singleton`, or by putting `@Inject constructor` on the impl classes.
- **Use one shared coroutine scope.** Instead of each impl creating its own `CoroutineScope(SupervisorJob() + Dispatchers.IO)`, provide a single app-wide one from a Hilt module.

A Java viewmodel then looks just like your current `SearchViewModel`:

```java
@Inject FavoritesViewModel(@NonNull FavoriteService favoriteService, @NonNull UserService userService) { ... }
favoriteService.add(image).exceptionally(this::postThrowable);
```

**Things to avoid in the service API:** `suspend` functions, `Flow`, Kotlin `Result`, and functions that return `Unit`. They're all awkward or unusable from Java. `@JvmOverloads` lets Java call functions that have default arguments.

## 3. Open question: where does the user come from?

The Google sign-in and credentials libraries in `app/build.gradle.kts` are still commented out, so nothing provides an `oauthKey` yet. I'd design `UserService.signIn(...)` to take that key as a parameter, so it doesn't care how sign-in happens. You'll also need to decide how the app knows the current user after a restart: either sign in again silently at startup, or save the user ID in `SharedPreferences` (the `preference` library is already a dependency). Your instructor may have a preferred approach.

## 4. Existing issues worth fixing first

I found these by reading the code. I didn't run a build, so they aren't confirmed by a compiler:

1. **`DatabaseModule.provideDatabase` has no `@Provides @Singleton`.** Hilt won't know how to create `SkyWatchDatabase`, so the build should fail as soon as anything injects a DAO.
2. **`UserRepositoryImpl` has no `@Inject constructor`.** Hilt can't create it as written.
3. **`Favorite`'s unique index is on `external_key` alone.** That means two different users can't favorite the same NASA image. You probably want `Index(value = ["user_id", "external_key"], unique = true)`. This changes the schema, so regenerate `1.json` and the DDL afterward. You might also add a `select(userId, externalKey)` DAO query so the UI can show whether an image is already a favorite.
4. **Mixed `@Singleton` imports.** `DatabaseModule` uses `javax.inject.Singleton`, while `UserRepositoryImpl` and `core`'s `ServiceModule` use `jakarta.inject.Singleton`. Recent Dagger versions should accept both, but pick one and run a build to confirm.
5. **Small cleanup:** `FavoriteDao.delete(favorites: Favorite)` should be `favorite` (singular), and there's a stray space in `delete (`.

If you'd like me to implement any of this, tell me which parts. Before I change anything, your working tree needs to be clean: `UserRepositoryImpl.kt` and `work/class-work.md` currently have uncommitted changes, and the project rules require you to commit or stash them first.