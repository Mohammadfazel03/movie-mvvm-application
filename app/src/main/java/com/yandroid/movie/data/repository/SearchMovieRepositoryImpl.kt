package com.yandroid.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yandroid.movie.data.DiscoverPagingSource
import com.yandroid.movie.data.SearchPagingSource
import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Genres
import com.yandroid.movie.data.model.Movie
import kotlinx.coroutines.flow.Flow

class SearchMovieRepositoryImpl(private val api: ApiService) : SearchMovieRepository {

    override fun searchMovie(query: String): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                SearchPagingSource(api, query)
            }
        ).flow
    }

    override fun discoverMovie(
        language: String?,
        region: String?,
        sortBy: String?,
        minReleaseDate: String?,
        maxReleaseDate: String?,
        releaseType: Int?,
        minVoteCount: Int?,
        maxVoteCount: Int?,
        minScore: Int?,
        maxScore: Int?,
        withPeople: String?,
        withGenres: String?,
        withKeywords: String?,
        watchRegion: String?
    ): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                DiscoverPagingSource(
                    api,
                    language,
                    region,
                    sortBy,
                    minReleaseDate,
                    maxReleaseDate,
                    releaseType,
                    minVoteCount,
                    maxVoteCount,
                    minScore,
                    maxScore,
                    withPeople,
                    withGenres,
                    withKeywords,
                    watchRegion
                )
            }
        ).flow
    }

    override suspend fun getGenres(): Genres = api.getGenres(APIKEY)

}