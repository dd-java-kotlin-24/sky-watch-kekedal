package edu.cnm.deepdive.skywatch.app.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import edu.cnm.deepdive.skywatch.app.model.entity.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insert(favorite: Favorite): Long

    @Delete
    suspend fun delete (favorite: Favorite): Int

    @Query(ORDER_BY_DESCRIPTION)
    fun selectOrderByDescription(userID: Long): LiveData<List<Favorite>>

    @Query(ORDER_BY_FAVORITED)
    fun selectOrderByFavorited(userID: Long): LiveData<List<Favorite>>
}

private const val ORDER_BY_DESCRIPTION = """
    SELECT
        *
    FROM
        Favorite
    WHERE
        user_id = :userID
    ORDER BY
        description ASC
"""

private const val ORDER_BY_FAVORITED = """
    SELECT
        *
    FROM
        Favorite
    WHERE
        user_id = :userID
    ORDER BY
        favorited DESC
"""