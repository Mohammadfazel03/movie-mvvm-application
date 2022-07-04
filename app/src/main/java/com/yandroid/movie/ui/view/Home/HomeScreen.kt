package com.yandroid.movie.ui.view.Home

import android.view.MotionEvent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.pager.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.math.MathUtils
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.ui.theme.PrimaryColor
import kotlinx.coroutines.*
import org.koin.androidx.compose.getViewModel
import kotlin.math.absoluteValue

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController) {
    val viewModel = getViewModel<HomeViewModel>()
    val isProgress = viewModel.isProgress.observeAsState(initial = true)
    val topMovie = viewModel.topRatedMovie.observeAsState(initial = null)
    val popularMovie = viewModel.popularMovie.observeAsState(initial = null)
    val trendingMovie = viewModel.trendingMovie.observeAsState(initial = null)
    val upcomingMovie = viewModel.upcomingMovie.observeAsState(initial = null)
    val errorText = viewModel.error.observeAsState(initial = null)

    BoxWithConstraints(modifier) {

        AnimatedVisibility(visible = isProgress.value, exit = fadeOut(), enter = fadeIn()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(
            visible = !(isProgress.value),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ConstraintLayout(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState(), true)
            ) {
                val (banner, upcoming_movie, top_movie, popular_movie, error_text) = createRefs()

                if (errorText.value.isNullOrEmpty()) {
                    HorizontalPagerWithOffsetTransition(
                        list = trendingMovie.value!!,
                        modifier = Modifier.constrainAs(banner) {
                            top.linkTo(parent.top, 8.dp)
                        },
                        count = 10,
                        autoChange = true,
                        onClick = {
                            navController.navigate("movie/$it")
                        }
                        )
                    MovieList(
                        title = "Top Movie",
                        movie = topMovie.value!!,
                        Modifier.constrainAs(top_movie) {
                            top.linkTo(banner.bottom, 8.dp)
                        }, movieClick = {
                            navController.navigate("movie/$it")
                        }, showMoreClick = {
                            navController.navigate("listMovie/TopMovie")
                        })
                    MovieList(
                        title = "Popular Movie",
                        movie = popularMovie.value!!,
                        Modifier.constrainAs(popular_movie) {
                            top.linkTo(top_movie.bottom, 8.dp)
                        }, movieClick = {
                            navController.navigate("movie/$it")
                        }, showMoreClick = {
                            navController.navigate("listMovie/PopularMovie")
                        })
                    MovieList(
                        title = "Upcoming Movie",
                        movie = upcomingMovie.value!!,
                        Modifier.constrainAs(upcoming_movie) {
                            top.linkTo(popular_movie.bottom, 8.dp)
                        }, movieClick = {
                            navController.navigate("movie/$it")
                        }, showMoreClick = {
                            navController.navigate("listMovie/UpcomingMovie")
                        })
                } else {
                    Text(text = "${errorText.value!!}\nPlease try again",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .constrainAs(error_text) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                absoluteRight.linkTo(parent.absoluteRight)
                                absoluteLeft.linkTo(parent.absoluteLeft)
                            }
                            .clickable {
                                viewModel.getListsOfMovie()
                            })
                }
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun HorizontalPagerWithOffsetTransition(
    list: List<Movie>,
    modifier: Modifier,
    count: Int,
    initialPage: Int = 0,
    autoChange: Boolean = false,
    duration: Long = 3000,
    onClick: (id: Int) -> Unit
) {
    val statePage = rememberPagerState(initialPage)

    if (autoChange)
        LaunchedEffect(Unit) {
            while (true) {
                delay(duration)
                yield()
                statePage.animateScrollToPage(
                    (statePage.currentPage + 1) % count
                )
            }
        }
    BoxWithConstraints(modifier) {
        HorizontalPager(
            count = count,
            state = statePage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f, false),
            contentPadding = PaddingValues(horizontal = maxWidth / 6)
        ) { page ->

            Card(
                Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                        lerp(
                            start = ScaleFactor(0.85f, 0.85f),
                            stop = ScaleFactor(1f, 1f),
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also {
                            scaleX = it.scaleX
                            scaleY = it.scaleY
                        }

                        // We animate the alpha, between 50% and 100%
                        alpha = MathUtils.lerp(
                            0.5f,
                            1f,
                            1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .fillMaxHeight()
                    .aspectRatio(2 / 3f),
                elevation = 8.dp,

                ) {
                AsyncImage(
                    model = list[page].getPosterUrl,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onClick(list[page].id)
                        },
                    contentScale = ContentScale.FillBounds
                )

            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MovieItem(width: Dp, movie: Movie, onClick: (id: Int) -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    val animScale by animateFloatAsState(targetValue = scale)

    Column(
        Modifier
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .width(width)
            .graphicsLayer {
                scaleY = animScale
                scaleX = animScale
            }
            .pointerInteropFilter {
                scale = when (it.action) {
                    MotionEvent.ACTION_DOWN -> 0.9f
                    else -> 1f
                }
                if (it.action == MotionEvent.ACTION_UP) {
                    onClick(movie.id)
                }
                true
            }) {
        Card(elevation = 4.dp) {
            AsyncImage(
                model = movie.getPosterUrl,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(width * 3 / 2),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            text = movie.title, maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = Color(0.549f, 0.549f, 0.549f, 1.0f),
            fontSize = 12.sp
        )

    }
}

@Composable
fun MovieList(
    title: String,
    movie: List<Movie>,
    modifier: Modifier = Modifier,
    movieClick: (id: Int) -> Unit,
    showMoreClick: (type: String) -> Unit
) {
    Column(modifier.padding(horizontal = 4.dp)) {
        Box(Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp).fillMaxWidth()) {
            Text(
                text = title, textAlign = TextAlign.Start,
                color = PrimaryColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopStart)
            )
            Row(modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable {
                    showMoreClick(title)
                }) {
                Text(text = "show more")
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
            }
        }
        LazyRow() {
            items(movie) {
                MovieItem(width = 120.dp, movie = it) {
                    movieClick(it)
                }
            }
        }
    }
}



