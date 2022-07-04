package com.yandroid.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yandroid.movie.data.*
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movie
import kotlinx.coroutines.flow.Flow

class ListMovieRepositoryImpl(private val api:ApiService):ListMovieRepository {

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

    override fun getTopRatedMovie(): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                TopPagingSource(
                    api
                )
            }
        ).flow
    }

    override fun getPopularMovie(): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                PopularPagingSource(
                    api
                )
            }
        ).flow
    }

    override fun getUpcomingMovie(): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                UpcomingPagingSource(
                    api
                )
            }
        ).flow    }



    override fun getSimilarMovie(movieId: Int): Flow<PagingData<Movie>> {
        return Pager(
            PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                SimilarPagingSource(
                    api = api, movieId = movieId
                )
            }
        ).flow    }

}