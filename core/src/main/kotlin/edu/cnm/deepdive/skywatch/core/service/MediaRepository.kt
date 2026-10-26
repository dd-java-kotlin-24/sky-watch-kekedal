package edu.cnm.deepdive.skywatch.core.service

import edu.cnm.deepdive.skywatch.core.model.SearchResultImage
import edu.cnm.deepdive.skywatch.core.model.dto.nasa.SearchItem
import java.time.OffsetDateTime

interface MediaRepository {

    /**
     *Submits a [list] of [SearchResultImage]s from a media provider, based on the criteria specified in the parameters that match the specified [searchFilter], [startDate], and [endDate].  At least one of [searchFilter], [startDate], or [endDate] must be non-'null', or an IllegalArgumentException is thrown.  If both [startDate] and [endDate] are non-null, [startDate] must not be greater than [endDate] or IllegalArgumentException is thrown.
     *
     * @param searchFilter // optional search string to match.
     * @param startDate // If null, all images are returned.
     * @param endDate // If null, all images are returned.
     * @return a [list] of [SearchResultImage]s.
     * @throws IllegalArgumentException // when the searchFilter is null, or the start and end dates are not available we would throw an exception.
     */
    fun search(
        searchFilter: String? = null,
        startDate: OffsetDateTime? = null,
        endDate: OffsetDateTime? = null,
    ): List<SearchResultImage>

}