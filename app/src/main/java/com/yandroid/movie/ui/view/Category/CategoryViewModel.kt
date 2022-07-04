package com.yandroid.movie.ui.view.Category

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandroid.movie.data.model.Genre
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.data.model.Movies
import com.yandroid.movie.data.repository.GenreRepository
import kotlinx.coroutines.*

class CategoryViewModel(private val repository: GenreRepository) : ViewModel() {

    private val mapOfGenres = HashMap<String, String>()
    val mapGenres = MutableLiveData<HashMap<String, String>>()
    val getGenres = MutableLiveData<List<Genre>>()
    val isProgress = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    init {
        getListOfGenres()
    }

    fun getListOfGenres() {
        isProgress.value = true
        error.value = ""
        viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler() { _, t ->
            error.postValue(t.message)
            isProgress.postValue(false)
        }) {
            val genres = repository.getGenres()
            if (!genres.statusMessage.isNullOrEmpty())
                throw Exception(genres.statusMessage)
            val getBackOfGenres = ArrayList<Deferred<Movies>>()
            repeat(genres.genres.size) {
                getBackOfGenres.add(async { repository.getPopularMovieGenre(genres.genres[it].id.toString()) })
            }
            val list = getBackOfGenres.awaitAll()
            repeat(list.size) { movieIndex ->
                if (!list[movieIndex].statusMessage.isNullOrEmpty())
                    throw Exception(list[movieIndex].statusMessage)
                repeat(genres.genres.size) { genreId ->
                    if (list[movieIndex].result[0].genresId.contains(genres.genres[genreId].id)) {
                        mapOfGenres[genres.genres[genreId].name] =
                            list[movieIndex].result[0].getBackdropUrl
                        cancel()
                    }
                }
            }
            getGenres.postValue(genres.genres)
            mapGenres.postValue(mapOfGenres)
            isProgress.postValue(false)
        }
    }

}