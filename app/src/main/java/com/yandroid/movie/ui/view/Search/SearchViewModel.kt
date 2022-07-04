package com.yandroid.movie.ui.view.Search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yandroid.movie.data.model.Genre
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.repository.SearchMovieRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val repository: SearchMovieRepository) : ViewModel() {

    val genres = MutableLiveData<List<Genre>>()
    val error = MutableLiveData<String>()
    val isProgressGenres = MutableLiveData<Boolean>()

    private val queryState = MutableStateFlow<HashMap<Int, String>>(hashMapOf(0 to ""))

    @OptIn(ExperimentalCoroutinesApi::class)
    val result = queryState.flatMapLatest {
        if (it.containsKey(0))
            return@flatMapLatest getMovieWithGenres(it.getValue(0))
        else
            return@flatMapLatest searchMovie(it.getValue(1))
    }

    init {
        getGenres()
    }

    fun search(query: String) {
        queryState.value = hashMapOf(1 to query)
    }
    fun discover(genres: String) {
        queryState.value = hashMapOf(0 to genres)
    }

    private fun searchMovie(query: String): Flow<PagingData<Movie>> {
        return repository.searchMovie(query).cachedIn(viewModelScope)
    }

    private fun getMovieWithGenres(listOfId: String): Flow<PagingData<Movie>> {
        return repository.discoverMovie(withGenres = listOfId).cachedIn(viewModelScope)
    }

    fun getGenres() {
        error.value = ""
        isProgressGenres.postValue(true)
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, t ->
            isProgressGenres.postValue(false)
            error.postValue(t.message)
        }) {
            val result = repository.getGenres()
            if (!result.statusMessage.isNullOrEmpty())
                throw Exception(result.statusMessage)
            genres.postValue(result.genres)
            isProgressGenres.postValue(false)
        }
    }

}