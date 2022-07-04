package com.yandroid.movie

import android.app.Application
import coil.request.ImageRequest
import com.yandroid.movie.data.api.ApiService
import com.yandroid.movie.data.api.createApiServiceInstance
import com.yandroid.movie.data.repository.*
import com.yandroid.movie.ui.view.Category.CategoryViewModel
import com.yandroid.movie.ui.view.Home.HomeViewModel
import com.yandroid.movie.ui.view.ListMovies.ListMovieViewModel
import com.yandroid.movie.ui.view.Movie.MovieViewModel
import com.yandroid.movie.ui.view.Search.SearchViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            single<ApiService> { createApiServiceInstance() }

            viewModel { SearchViewModel(SearchMovieRepositoryImpl(get())) }
            viewModel { HomeViewModel(BannerRepositoryImpl(get())) }
            viewModel { CategoryViewModel(GenreRepositoryImpl(get())) }
            viewModel { MovieViewModel(MovieRepositoryImpl(get())) }
            viewModel { ListMovieViewModel(ListMovieRepositoryImpl(get())) }
        }

        startKoin {
            androidContext(this@App)
            // use modules
            modules(module)
        }
    }
}