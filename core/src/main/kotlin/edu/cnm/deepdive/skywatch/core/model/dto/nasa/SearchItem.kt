package edu.cnm.deepdive.skywatch.core.model.dto.nasa

import com.squareup.moshi.JsonClass
import java.net.URL

@JsonClass(generateAdapter = true)
data class SearchItem(
    val mediaCollectionUrl: URL,
    val dat: List<ItemData>,
    val links: List<Link>,

)
