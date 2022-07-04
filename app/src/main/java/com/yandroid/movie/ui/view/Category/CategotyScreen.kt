package com.yandroid.movie.ui.view.Category

import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yandroid.movie.data.model.Genre
import com.yandroid.movie.ui.theme.PrimaryColor
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryScreen(modifier: Modifier, navController: NavController) {
    val viewModel = getViewModel<CategoryViewModel>()
    val isProgress = viewModel.isProgress.observeAsState(initial = true)
    val posterOfGenres = viewModel.mapGenres.observeAsState(initial = null)
    val listOfGenres = viewModel.getGenres.observeAsState(initial = null)
    val errorText = viewModel.error.observeAsState(initial = null)
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedVisibility(visible = isProgress.value, exit = fadeOut(), enter = fadeIn()) {
            CircularProgressIndicator()
        }
        AnimatedVisibility(
            visible = !(isProgress.value),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (errorText.value.isNullOrEmpty()) {
                LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                    items(listOfGenres.value!!.size) {
                        ItemOfGenre(
                            genre = listOfGenres.value!!.get(it),
                            url = posterOfGenres.value!!.getValue(listOfGenres.value!!.get(it).name),
                            onClick = {
                                navController.navigate("listMovie/MovieWithGenre?id=$it")
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "${errorText.value!!}\nPlease try again",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        viewModel.getListOfGenres()
                    })
            }
        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ItemOfGenre(genre:Genre, url: String, onClick: (id:Int) -> Unit) {
    var background by remember { mutableStateOf(PrimaryColor) }
    var scale by remember { mutableStateOf(1f) }
    val animScale by animateFloatAsState(targetValue = scale)

    Box {
        Card(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.7f)
                .padding(4.dp)
                .graphicsLayer {
                    scaleY = animScale
                    scaleX = animScale
                }
                .pointerInteropFilter { it ->
                    scale = when (it.action) {
                        MotionEvent.ACTION_DOWN -> 0.9f
                        else -> 1f
                    }
                    if (it.action == MotionEvent.ACTION_UP)
                        onClick(genre.id)
                    true
                },
            shape = RoundedCornerShape(8.dp),
            elevation = 8.dp
        ) {
            BoxWithConstraints {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .allowHardware(false)
                        .build(),
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds,
                    onSuccess = { result ->
                        // Create the palette on a background thread.
                        Palette.Builder(result.result.drawable.toBitmap()).generate { palette ->
                            if (palette != null && palette.darkVibrantSwatch != null) {
                                background = Color(palette.darkVibrantSwatch!!.rgb)
                            }
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(maxWidth / 3, maxWidth / 2)
                        .background(
                            brush = Brush.horizontalGradient(
                                (0.2f to background.copy(alpha = 1f)),
                                (0.4f to background.copy(alpha = 0.8f)),
                                (0.6f to background.copy(alpha = 0.6f)),
                                (0.8f to background.copy(alpha = 0.4f)),
                                (1f to background.copy(alpha = 0.2f)),
                            )
                        )
                ) {
                    Text(
                        text = genre.name,
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 4.dp),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}
