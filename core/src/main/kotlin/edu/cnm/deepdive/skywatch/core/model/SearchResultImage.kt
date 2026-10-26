package edu.cnm.deepdive.skywatch.core.model

import java.net.URL

data class SearchResultImage(
    val description: String,
    val keyworkds: List<String>,
    val image: URL,
    val thumbnail: URL,
    )
