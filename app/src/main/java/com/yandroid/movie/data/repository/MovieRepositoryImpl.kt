package com.yandroid.movie.data.repository

import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movie

class MovieRepositoryImpl(private val api: ApiService) : MovieRepository {
    override suspend fun getMovieDetails(id:Int): Movie = api.getMovie(id, APIKEY)
}