package com.yandroid.movie.data.repository

import com.yandroid.movie.data.model.Genre
import com.yandroid.movie.data.model.Genres
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies

interface GenreRepository {

    suspend fun getGenres(): Genres

    suspend fun getPopularMovieGenre(withGenre: String): Movies
}