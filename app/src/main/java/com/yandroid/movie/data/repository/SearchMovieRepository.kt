package com.yandroid.movie.data.repository

import androidx.paging.PagingData
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Genres
import com.yandroid.movie.data.model.Movie
import kotlinx.coroutines.flow.Flow

interface SearchMovieRepository {

     fun searchMovie(query: String):Flow<PagingData<Movie>>

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
    ):Flow<PagingData<Movie>>

    suspend fun getGenres(): Genres

}