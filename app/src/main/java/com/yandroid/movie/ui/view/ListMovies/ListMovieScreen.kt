package com.yandroid.movie.ui.view.ListMovies

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.yandroid.movie.R
import com.yandroid.movie.ui.theme.PrimaryColor
import com.yandroid.movie.ui.view.Search.LoadingItem
import com.yandroid.movie.ui.view.Search.MovieItem
import org.koin.androidx.compose.getViewModel


@Composable
fun ListMovieScreen(type: String, id: Int?, navController: NavController) {

    val viewModel = getViewModel<ListMovieViewModel>()

    val movieItems = when (type) {
        "TopMovie" -> viewModel.getTopMovie().collectAsLazyPagingItems()
        "PopularMovie" -> viewModel.getPopularMovie().collectAsLazyPagingItems()
        "UpcomingMovie" -> viewModel.getUpcomingMovie().collectAsLazyPagingItems()
        "SimilarMovie" -> viewModel.getSimilarMovie(id!!).collectAsLazyPagingItems()
        "MovieWithGenre" -> viewModel.getMovieWithGenre(id.toString()).collectAsLazyPagingItems()
        else -> null
    }

    movieItems?.let {


        BoxWithConstraints(Modifier.fillMaxSize()) {

            AnimatedVisibility(
                visible = it.loadState.refresh is LoadState.Loading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    LoadingItem()
                }
            }

            AnimatedVisibility(
                visible = it.loadState.refresh is LoadState.Error,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_error_emot),
                            contentDescription = "",
                            Modifier
                                .fillMaxWidth(0.6f)
                                .aspectRatio(1f)
                        )
                        Text(
                            text = "your connection have some problem",
                            Modifier,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Try Again",
                            Modifier
                                .clickable { movieItems.refresh() },
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = PrimaryColor
                        )
                    }
                }
            }


            AnimatedVisibility(
                visible = it.loadState.refresh is LoadState.NotLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .background(Color(0xD000000))
                ) {
                    if (it.loadState.prepend is LoadState.Loading) {
                        item {
                            LoadingItem()
                        }
                    } else if (it.loadState.prepend is LoadState.Error) {
                        item {
                            ErrorItem("Some thing went Wrong") { it.retry() }
                        }
                    }
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
                    if (it.loadState.append is LoadState.Loading) {
                        item {
                            LoadingItem()
                        }
                    } else if (it.loadState.append is LoadState.Error) {
                        item {
                            ErrorItem("Some thing went Wrong") { it.retry() }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ErrorItem(message: String = "error", click: () -> Unit) {
    Box(
        Modifier
            .padding(8.dp, 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = message, Modifier.align(Alignment.CenterStart), fontSize = 16.sp)
        Text(
            text = "Try Again",
            Modifier
                .align(Alignment.CenterEnd)
                .clickable { click() }, fontSize = 16.sp, color = PrimaryColor
        )
    }
}

