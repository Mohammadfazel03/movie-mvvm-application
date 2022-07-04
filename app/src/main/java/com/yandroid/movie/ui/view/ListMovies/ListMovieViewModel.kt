package com.yandroid.movie.ui.view.ListMovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.repository.ListMovieRepository
import kotlinx.coroutines.flow.Flow

class ListMovieViewModel(private val repository: ListMovieRepository) : ViewModel() {

    fun getTopMovie(): Flow<PagingData<Movie>> =
        repository.getTopRatedMovie().cachedIn(viewModelScope)
    fun getPopularMovie(): Flow<PagingData<Movie>> =
        repository.getPopularMovie().cachedIn(viewModelScope)
    fun getUpcomingMovie(): Flow<PagingData<Movie>> =
        repository.getUpcomingMovie().cachedIn(viewModelScope)
    fun getSimilarMovie(movieId:Int): Flow<PagingData<Movie>> =
        repository.getSimilarMovie(movieId).cachedIn(viewModelScope)
    fun getMovieWithGenre(id:String): Flow<PagingData<Movie>> =
        repository.discoverMovie(withGenres = id).cachedIn(viewModelScope)

}