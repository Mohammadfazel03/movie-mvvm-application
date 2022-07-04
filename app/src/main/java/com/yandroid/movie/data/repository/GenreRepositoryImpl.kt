package com.yandroid.movie.data.repository

import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Genres
import com.yandroid.movie.data.model.Movies

class GenreRepositoryImpl(private val api: ApiService):GenreRepository {

    override suspend fun getGenres(): Genres = api.getGenres(APIKEY)
    override suspend fun getPopularMovieGenre(withGenre:String): Movies = api.discoverMovie(key = APIKEY, sortBy = "popularity.desc", withGenres = withGenre, page = 1)
}