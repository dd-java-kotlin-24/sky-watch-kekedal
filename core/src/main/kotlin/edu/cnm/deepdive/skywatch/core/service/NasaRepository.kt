package edu.cnm.deepdive.skywatch.core.service

import edu.cnm.deepdive.skywatch.core.model.SearchResultImage
import java.time.OffsetDateTime

internal object NasaRepository : MediaRepository {

    private val api: NasaMediaApi

    init {
        TODO("initialize api using Moshi, OkHttp, and Retrofit")
    }

    //  /**
    //     *Submits a search request to the NASA media search provider, returning the results as a [list] of [SearchResultImage]s, that is , as a list of image reference. The order of the results returned by this function is not documented by the NASA media API; nor are there search parameters that can be used to change this order; thus, the order of returned results is not defined.
    //     *
    //     * At least one of [searchFilter], [startDate], or [endDate] must be non-'null', or an IllegalArgumentException will be thrown.   If a non-`null` value is not provided for either [startDate] or [endDate], and [searchFilter] is [IllegalArgumentException] is also thrown when non-`null` values are provided for both [startDate] and [endDate], but `startDate > endDate`.
    //     *
    //     * One implication of the above is that a date-time range may be specified by a single endpoint. for example a `null` [startDate] combined with  a non-`null` [endDate] will return images for _any_ date-time less than or equal to [endDate].
    //     *
    //     * If both a date range (one -or two-sided) and a non-`null', non-empty [searchFilter] are specified, the two criteria are combined with a logical AND.
    //     *
    //     * The NASA media API uses only the year of a specified date range in its search criteria. the date-time parameters to this fiunction must still be specified as [OffsetDateTime]s instances, but only the year portion is used.
    //     *
    //     * Paging is not supported by this API; the maximum size of the returned list&mdash; as well as any truncation behaviour &mdash; is not documented by the NASA media API and can therefore not be assumed.
    //     *
    //     * @param searchFilter // optional search string to match.
    //     * @param startDate // inclusive start of optional date range filter.
    //     * @param endDate // inclusive end of optional date range filter.
    //     * @return a [list] of [SearchResultImage]s.
    //     * @throws IllegalArgumentException // If:
    //     *
    //     * - all of [searchFilter], [startDate],and [endDate] are `null`.
    //     * -[startDate] and [endDate] are `null`, and [searchFilter] is blank.
    //     * - non-`null` values are provided for both [startDate] and [endDate], but
    //     * -`startDate > endDate` (using [OffsetDateTime.isAftercompareTo]).
    //     *
    //     * @throws UncheckedIOException (specific type specific by implementation) for any other network-access or service  communication failure.

    /**
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
    ): List<SearchResultImage> {
        TODO("Not yet implemented")
    }
}

// TODO: Implement any helper classes, constants, etc.