package edu.cnm.deepdive.skywatch.core.service

import edu.cnm.deepdive.skywatch.core.model.dto.nasa.SearchItem
import java.time.OffsetDateTime

internal object NasaRepository : MediaRepository {

    // TODO:  Initialize Moshi, OkHTTP client with logging interceptor, and Retrofit;
    //  then use Retrofit unstance to create an implementation (and an instance of it)
    //  of the NasaMediaApi interface.

    /**
     * TODO Add contract description.
     *
     * @param searchFilter
     * @param startDate
     * @param endDate
     *
     */
    override fun search(
        searchFilter: String?,
        startDate: OffsetDateTime? ,
        endDate: OffsetDateTime? ,
    ): Array<SearchItem> {
        TODO("Not yet implemented")
    }
}