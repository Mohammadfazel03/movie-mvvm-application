package com.yandroid.movie.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies
import kotlinx.coroutines.flow.Flow

interface BannerRepository {

    suspend fun getTopRatedMovie(): Movies

    suspend fun getPopularMovie(): Movies

    suspend fun getUpcomingMovie(): Movies

    suspend fun getTrendingMovie(): Movies
}