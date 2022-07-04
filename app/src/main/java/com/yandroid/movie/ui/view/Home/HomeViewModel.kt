package com.yandroid.movie.ui.view.Home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies
import com.yandroid.movie.data.repository.BannerRepository
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

class HomeViewModel(private val repository: BannerRepository) : ViewModel() {

    val isProgress = MutableLiveData<Boolean>()
    val topRatedMovie = MutableLiveData<List<Movie>>()
    val upcomingMovie = MutableLiveData<List<Movie>>()
    val popularMovie = MutableLiveData<List<Movie>>()
    val trendingMovie = MutableLiveData<List<Movie>>()
    val error = MutableLiveData<String>()

    init {
        getListsOfMovie()
    }

    fun getListsOfMovie() {
        isProgress.value = true
        error.value = ""

        viewModelScope.launch(Dispatchers.IO +
                CoroutineExceptionHandler() { _, t: Throwable ->
                    error.postValue(t.message)
                    isProgress.postValue(false)
                }) {
            val top = async { repository.getTopRatedMovie() }
            val trending = async { repository.getTrendingMovie() }
            val popular = async { repository.getPopularMovie() }
            val upcoming = async { repository.getUpcomingMovie() }
            trendingMovie.postValue(trending.await().let {
                if (!it.statusMessage.isNullOrEmpty())
                    throw Exception(it.statusMessage)
                else
                    it.result
            })
            upcomingMovie.postValue(upcoming.await().let {
                if (!it.statusMessage.isNullOrEmpty())
                    throw Exception(it.statusMessage)
                else
                    it.result
            })
            topRatedMovie.postValue(top.await().let {
                if (!it.statusMessage.isNullOrEmpty())
                    throw Exception(it.statusMessage)
                else
                    it.result
            })
            popularMovie.postValue(popular.await().let {
                if (!it.statusMessage.isNullOrEmpty())
                    throw Exception(it.statusMessage)
                else
                    it.result
            })
            isProgress.postValue(false)
        }
    }
}
