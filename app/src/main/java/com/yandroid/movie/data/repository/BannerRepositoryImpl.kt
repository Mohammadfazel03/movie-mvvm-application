package com.yandroid.movie.data.repository

import com.yandroid.movie.data.api.APIKEY
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.model.Movies

class BannerRepositoryImpl(private val api: ApiService) : BannerRepository {

    override suspend fun getTopRatedMovie(): Movies = api.getTopRatedMovie(APIKEY,1)

    override suspend fun getPopularMovie(): Movies = api.getPopularMovie(APIKEY,1)

    override suspend fun getUpcomingMovie(): Movies = api.getUpcomingMovie(APIKEY, 1)

    override suspend fun getTrendingMovie(): Movies = api.getTrendingMovie(APIKEY)

}