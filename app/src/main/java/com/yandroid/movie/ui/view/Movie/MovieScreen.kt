package com.yandroid.movie.ui.view.Movie

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yandroid.movie.R
import com.yandroid.movie.data.model.Credit
import com.yandroid.movie.data.model.Poster
import com.yandroid.movie.ui.theme.PrimaryColor
import com.yandroid.movie.ui.theme.SecondaryColor
import com.yandroid.movie.ui.view.Home.MovieList
import org.koin.androidx.compose.getViewModel

@Composable
fun MovieScreen(id: Int, navController: NavController) {

    val viewModel = getViewModel<MovieViewModel>()
    viewModel.getMovieDetails(id)
    val isProgress = viewModel.isProgress.observeAsState(initial = true)
    val error = viewModel.error.observeAsState(initial = null)
    val movie = viewModel.movieDetails.observeAsState(initial = null)


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
        if (error.value.isNullOrEmpty() && movie.value != null) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(rememberScrollState(), true)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(500 / 281f)
                ) {
                    AsyncImage(
                        model = movie.value!!.getBackdropUrl, contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    Spacer(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.White
                                    )
                                )
                            )
                            .fillMaxWidth()
                            .fillMaxHeight(0.05f)
                            .align(Alignment.BottomCenter)
                    )
                }
                Text(
                    text = movie.value!!.title,
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Row(Modifier.padding(vertical = 2.dp, horizontal = 4.dp)) {
                    RantingBar(
                        selectColor = SecondaryColor,
                        unSelectColor = Color.LightGray,
                        max = 10,
                        current = movie.value!!.voteAverage
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_time),
                        contentDescription = "",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${movie.value!!.runtime} min",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                LazyRow(
                    Modifier.padding(vertical = 2.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    items(movie.value!!.genres) {
                        Box(
                            Modifier
                                .border(1.dp, Color.Gray)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(5))
                        ) {
                            Text(text = it.name, color = Color.Gray, fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))

                    }
                }

                Text(
                    text = "Storyline",
                    color = Color.DarkGray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 4.dp,
                        start = 4.dp,
                        end = 4.dp
                    )
                )

                Text(
                    text = movie.value!!.overview ?: "",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Justify,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 5,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )

                if (movie.value!!.images.backdrops.isNotEmpty()) {
                    ImageList(title = "Images", list = movie.value!!.images.backdrops)
                }

                if (movie.value!!.credits.cast.isNotEmpty()) {

                    CastList(
                        title = "Cast",
                        list = movie.value!!.credits.cast.subList(
                            0,
                            if (movie.value!!.credits.cast.size > 20) 19 else movie.value!!.credits.cast.size
                        )
                    )
                }

                if (movie.value!!.similar.result.isNotEmpty()) {
                    MovieList(
                        title = "Similar Movie",
                        movie = movie.value!!.similar.result,
                        movieClick = {
                            navController.navigate("movie/$it")
                        },
                        showMoreClick = {
                            navController.navigate("listMovie/SimilarMovie?id=${movie.value!!.id}")
                        })
                }

            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "${error.value!!}\nPlease try again",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            viewModel.getMovieDetails(id)
                        })
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CastItem(width: Dp, credit: Credit) {
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
                true
            }) {
        Card(elevation = 4.dp) {
            AsyncImage(
                model = credit.getProfilePath,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(width * 3 / 2),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(
            text = credit.name, maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = Color(0.549f, 0.549f, 0.549f, 1.0f),
            fontSize = 12.sp
        )

    }
}

@Composable
fun CastList(title: String, list: List<Credit>, modifier: Modifier = Modifier) {
    Column(modifier.padding(horizontal = 4.dp)) {
        Text(
            text = title, textAlign = TextAlign.Start,
            color = PrimaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )
        LazyRow() {
            items(list) {
                CastItem(width = 120.dp, credit = it)
            }
        }
    }
}

@Composable
fun ImageList(title: String, list: List<Poster>, modifier: Modifier = Modifier) {
    val isOpen = remember { mutableStateOf(true) }
    val path = remember { mutableStateOf("") }
    Column(modifier.padding(horizontal = 4.dp)) {
        Text(
            text = title, textAlign = TextAlign.Start,
            color = PrimaryColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)
        )
        LazyRow() {
            items(list) {
                Box(modifier = Modifier.padding(5.dp)) {
                    Card(elevation = 2.dp, shape = RoundedCornerShape(5.dp)) {
                        AsyncImage(
                            model = it.getPath,
                            contentDescription = null,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(140.dp)
                                .aspectRatio(580 / 281f)
                                .clickable {
                                    path.value = it.getPath
                                    isOpen.value = true
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RantingBar(
    countStars: Int = 5,
    max: Int = 10,
    current: Double = 3.5,
    selectColor: Color = Color.Yellow,
    unSelectColor: Color = Color.Black,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        var c = current;
        repeat(countStars) {
            if (c - (max / countStars) > 0)
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    tint = selectColor
                )
            else if (c in 0.0..(max / countStars).toDouble()) {
                val list = ArrayList<Color>()
                repeat((c * 10).toInt()) {
                    list.add(selectColor)
                }
                repeat(max / countStars * 10 - (c * 10).toInt()) {
                    list.add(unSelectColor)
                }
                val brush =
                    Brush.horizontalGradient(list)
                Icon(imageVector = Icons.Rounded.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer(alpha = 0.99f)
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(brush, blendMode = BlendMode.SrcAtop)
                            }
                        })
            } else {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null, tint = unSelectColor
                )
            }
            c -= max / countStars
        }
    }
}