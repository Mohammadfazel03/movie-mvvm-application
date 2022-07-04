package com.yandroid.movie.ui.view.ListMovies

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yandroid.movie.ui.view.Search.LoadingItem
import com.yandroid.movie.ui.view.Search.MovieItem
import org.koin.androidx.compose.getViewModel

@Composable
fun ListMovieScreen(type:String , id:Int? , navController:NavController) {

    val viewModel = getViewModel<ListMovieViewModel>()

    val movieItems = when(type)
    {
        "TopMovie"->viewModel.getTopMovie().collectAsLazyPagingItems()
        "PopularMovie"->viewModel.getPopularMovie().collectAsLazyPagingItems()
        "UpcomingMovie" ->viewModel.getUpcomingMovie().collectAsLazyPagingItems()
        "SimilarMovie"->viewModel.getSimilarMovie(id!!).collectAsLazyPagingItems()
        "MovieWithGenre"->viewModel.getMovieWithGenre(id.toString()).collectAsLazyPagingItems()
        else -> null
    }

    movieItems?.let {
        LazyColumn(Modifier.fillMaxSize().background(Color(0xD000000))) {
            items(it) { movie ->
                MovieItem(
                    movie = movie!!, modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(4 / 2f)
                ) {
                    navController.navigate("movie/$it")
                }
            }


            it.apply {
                when {
                    loadState.refresh is
                            LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.append is
                            LoadState.Loading -> {
                        item { LoadingItem() }
                    }
                    loadState.refresh is
                            LoadState.Error -> {
                        item { Text(text = "error") }
                    }
                    loadState.append is
                            LoadState.Error -> {
                        item { Text(text = "error") }
                    }
                    else -> {
                        item { Text(text = "error") }
                    }
                }
            }
        }
    }
}