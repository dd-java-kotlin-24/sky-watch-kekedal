package edu.cnm.deepdive.skywatch.core.service

interface NasaMediaApi {

    fun search(
        query: String = "",
        mediaType: String = "image",
        startYear: Int = 1900,
        endYear: Int = 2100
    ): <Call>
}