package com.yandroid.movie.data.repository

import com.yandroid.movie.data.model.Movie

interface MovieRepository {
    suspend fun getMovieDetails(id:Int) :Movie
}