package com.yandroid.movie.data.repository

import androidx.paging.PagingData
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies
import kotlinx.coroutines.flow.Flow

interface ListMovieRepository {

    fun discoverMovie(
        language: String? = null,
        region: String? = null,
        sortBy: String? = null,
        minReleaseDate: String? = null,
        maxReleaseDate: String? = null,
        releaseType: Int? = null,
        minVoteCount: Int? = null,
        maxVoteCount: Int? = null,
        minScore: Int? = null,
        maxScore: Int? = null,
        withPeople: String? = null,
        withGenres: String? = null,
        withKeywords: String? = null,
        watchRegion: String? = null
    ): Flow<PagingData<Movie>>

     fun getTopRatedMovie(): Flow<PagingData<Movie>>

     fun getPopularMovie(): Flow<PagingData<Movie>>

     fun getUpcomingMovie(): Flow<PagingData<Movie>>

     fun getSimilarMovie(movieId:Int): Flow<PagingData<Movie>>
}