package com.yandroid.movie.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movie
import java.lang.Exception

class DiscoverPagingSource(
    private val api: ApiService,
    private val language: String? = null,
    private val region: String? = null,
    private val sortBy: String? = null,
    private val minReleaseDate: String? = null,
    private val maxReleaseDate: String? = null,
    private val releaseType: Int? = null,
    private val minVoteCount: Int? = null,
    private val maxVoteCount: Int? = null,
    private val minScore: Int? = null,
    private val maxScore: Int? = null,
    private val withPeople: String? = null,
    private val withGenres: String? = null,
    private val withKeywords: String? = null,
    private val watchRegion: String? = null
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val currentPage = params.key ?: 1
            val result = api.discoverMovie(
                key = APIKEY,
                language = language,
                withPeople = withPeople,
                region = region,
                sortBy = sortBy,
                minReleaseDate = minReleaseDate,
                maxReleaseDate = maxReleaseDate,
                releaseType = releaseType,
                minVoteCount = minVoteCount,
                maxVoteCount = maxVoteCount,
                minScore = minScore,
                maxScore = maxScore,
                withGenres = withGenres,
                withKeywords = withKeywords,
                watchRegion = watchRegion,
                page = currentPage
            )

            if (!result.statusMessage.isNullOrEmpty())
                throw Exception(result.statusMessage)

            LoadResult.Page(
                result.result,
                if (currentPage == 1) null else currentPage - 1,
                if (currentPage == result.totalPage) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}