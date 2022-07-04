package com.yandroid.movie.ui.view.Movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.repository.MovieRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MovieViewModel(private val repository: MovieRepository) : ViewModel() {
    val isProgress = MutableLiveData<Boolean>()
    val movieDetails = MutableLiveData<Movie>()
    val error = MutableLiveData<String>()

    fun getMovieDetails(id:Int) {
        isProgress.postValue(true)
        error.value = ""
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            error.postValue(throwable.message)
            isProgress.postValue(false)
        }) {
            val movie = repository.getMovieDetails(id)
            if (!movie.statusMessage.isNullOrEmpty())
                throw Exception(movie.statusMessage)
            movieDetails.postValue(movie)
            isProgress.postValue(false)
        }
    }
}