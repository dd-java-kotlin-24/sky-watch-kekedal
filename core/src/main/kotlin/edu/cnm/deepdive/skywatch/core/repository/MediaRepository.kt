package edu.cnm.deepdive.skywatch.core.repository

import edu.cnm.deepdive.skywatch.core.model.SearchResultImage
import java.time.OffsetDateTime

interface MediaRepository {

    /**
     *Submits a from a media service, returning the results as a [list] of [SearchResultImage]s, based on the criteria specified in the parameters that match the specified [searchFilter], [startDate], and [endDate].  The order of the results returned by this function is left to the implementation; higher-level ordering requirements should be addressed by consumers (direct or indirect) of this function.
     *
     * At least one of [searchFilter], [startDate], or [endDate] must be non-'null', or an IllegalArgumentException is thrown.  If both [startDate] and [endDate] are non-null, [startDate] must not be greater than [endDate] or an IllegalArgumentException is thrown. If a non-`null` value is not provided for either [startDate] or [endDate], and [searchFilter] is non-`null` but [searchFilter]is blank, [IllegalArgumentException] is also thrown when non-`null` [startDate] and [endDate], but `startDate > endDate`.
     *
     * One implication of the above is that a date-time range may be specified by a single endpoint. for example a `null` [startDate] combined with  a non-`null` [endDate] will return images for _any_ date-time less than or equal to [endDate].
     *
     * if both a date range (one -or two-sided) and a non-`null', non-empty [searchFilter] are specified, the two criteria are combined with a logical AND.
     *
     * Paging is not supported by this API the maximum size of the returned list is provider- or implementation- specified.
     *
     * @param searchFilter // optional search string to match.
     * @param startDate // inclusive start of optional date range filter.
     * @param endDate // inclusive end of optional date range filter.
     * @return a [list] of [SearchResultImage]s.
     * @throws IllegalArgumentException // If:
     * - all of [searchFilter], [startDate],and [endDate] are `null`.
     * -[startDate] and [endDate] are `null`, and [searchFilter] is blank.
     * -`startDate > endDate` (using [OffsetDateTime.isAftercompareTo]).
     *
     * @throws Exception (specific type specific by implementation) for any other network-access or service  communication failure.
     */
    suspend fun search(
        searchFilter: String? = null,
        startDate: OffsetDateTime? = null,
        endDate: OffsetDateTime? = null,
    ): List<SearchResultImage>

}