package com.yandroid.movie.ui.view.Search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.yandroid.movie.R
import com.yandroid.movie.data.model.Genre
import com.yandroid.movie.data.model.Movie
import com.yandroid.movie.ui.theme.*
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(modifier: Modifier,navController: NavController) {

    val viewModel = getViewModel<SearchViewModel>()


    val isProgressGenres = viewModel.isProgressGenres.observeAsState(initial = true)
    val errorText = viewModel.error.observeAsState(initial = null)
    val genres = viewModel.genres.observeAsState(initial = null)

    val selectGenres = rememberSaveable { mutableStateOf("") }

    val movieItems = viewModel.result.collectAsLazyPagingItems()


    Scaffold(
        topBar = {
            SearchBarView(onChange = {
                viewModel.search(it)
            })
        }, modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = !isProgressGenres.value,
                exit = fadeOut(),
                enter = fadeIn()
            ) {
                if (errorText.value.isNullOrEmpty()) {
                    LazyRow(
                        Modifier
                            .background(PrimaryColor)
                            .padding(vertical = 4.dp)
                    ) {
                        genres.value?.let { it1 ->
                            items(it1.size ) {
                                GenresItem(
                                    onClick = { id ->
                                        selectGenres.value = when {
                                            selectGenres.value.contains("$id,") ->
                                                selectGenres.value.replace("$id,", "")
                                            else -> selectGenres.value + "$id,"
                                        }
                                        viewModel.discover(selectGenres.value)
                                    },
                                    genre = genres.value!![it],
                                    unSelectedColor = SecondaryColor.copy(0.2f),
                                    selectedColor = SecondaryColor,
                                    unSelectedContentColor = SecondaryColor.copy(0.7f),
                                    selectedContentColor = White.copy(0.7f)
                                )
                            }
                        }
                    }
                }
            }

                if (errorText.value.isNullOrEmpty()) {
                    LazyColumn(Modifier.background(Color(0xD000000))) {
                        items(movieItems) { movie ->
                            MovieItem(
                                movie = movie!!, modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(4 / 2f)
                            ) {
                                navController.navigate("movie/$it")
                            }
                        }

                        movieItems.apply {
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
                            }
                        }

                    }
                } else {
                    Text(
                        text = "${errorText.value!!}\nPlease try again",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.clickable {
                            viewModel.getGenres()
                            movieItems.retry()
                        })
                }

        }
    }

}

@Composable
fun GenresItem(
    onClick: (id: Int) -> Unit,
    genre: Genre,
    selectedColor: Color = Color(0xff6200ea),
    unSelectedColor: Color = Color(0xFFFFD600),
    selectedContentColor: Color = Color(0xFFDD2C00),
    unSelectedContentColor: Color = Color(0xFF00BFA5)
) {
    var selected by rememberSaveable {
        mutableStateOf(false)
    }

    val backgroundAnim by animateColorAsState(targetValue = if (selected) selectedColor else unSelectedColor)
    val foregroundAnim by animateColorAsState(targetValue = if (selected) selectedContentColor else unSelectedContentColor)
    Card(
        Modifier
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = genre.name, color = foregroundAnim, modifier = Modifier
                .background(backgroundAnim)
                .clickable {
                    onClick(genre.id)
                    selected = !selected
                }
                .padding(horizontal = 8.dp, vertical = 4.dp), fontSize = 16.sp
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBarView(onChange: (string: String) -> Unit) {
    var searchString by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    Card(
        backgroundColor = PrimaryColor, modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RectangleShape,
        elevation = 0.dp
    ) {
        BasicTextField(
            value = searchString,
            onValueChange = { string ->
                onChange(string)
                searchString = string
            },
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize()
                .background(Color(0x27000000)),
            singleLine = true,
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            decorationBox = @Composable { innerTextField ->
                TextFieldDefaults.TextFieldDecorationBox(
                    value = searchString,
                    innerTextField = innerTextField,
                    enabled = true,
                    singleLine = true,
                    visualTransformation = VisualTransformation.None,
                    interactionSource = remember { MutableInteractionSource() },
                    leadingIcon = @Composable {
                        Icon(
                            painterResource(id = R.drawable.ic_search),
                            contentDescription = "",
                            tint = TextColor
                        )
                    },
                    contentPadding = PaddingValues(0.dp)
                )
            }, cursorBrush = SolidColor(TextColor),
            textStyle = TextStyle(color = TextColor)
        )
    }
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClick: (id: Int) -> Unit
) {
    val i = rememberAsyncImagePainter(model = movie.getPosterUrl)

    BoxWithConstraints(modifier.padding(horizontal = 8.dp, vertical = 6.dp).clickable {
        onClick(movie.id)
    }) {
        ConstraintLayout(Modifier.fillMaxSize()) {
            val (image_poster, title_text, overview_text, genres_text, score_text, card_container) = createRefs()
            createVerticalChain(
                title_text,
                genres_text,
                overview_text,
                chainStyle = ChainStyle.Packed(0.34f)
            )
            Card(elevation = 4.dp,
                shape = RoundedCornerShape(5),
                modifier = Modifier
                    .fillMaxHeight(0.95f)
                    .aspectRatio(2 / 3f)
                    .constrainAs(image_poster) {
                        top.linkTo(parent.top)
                        absoluteLeft.linkTo(parent.absoluteLeft, 8.dp)
                    }
                    .zIndex(0.1f)) {
                Image(
                    painter = i,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

            Card(
                shape = RoundedCornerShape(5),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.95f)
                    .constrainAs(card_container) {
                        bottom.linkTo(parent.bottom)
                    }) {}

            Text(
                text = "${movie.voteAverage}", color = White, fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .zIndex(0.2f)
                    .clip(CircleShape)
                    .background(PrimaryDarkColor)
                    .padding(8.dp)
                    .constrainAs(score_text) {
                        absoluteLeft.linkTo(image_poster.absoluteRight)
                        absoluteRight.linkTo(image_poster.absoluteRight)
                        linkTo(
                            bottom = image_poster.bottom,
                            top = image_poster.top,
                            bias = 0.9f
                        )

                    }
            )

            Text(
                text = movie.title,
                Modifier.constrainAs(title_text) {
                    width = Dimension.fillToConstraints
                    absoluteLeft.linkTo(score_text.absoluteRight, 4.dp)
                    absoluteRight.linkTo(card_container.absoluteRight, 4.dp)
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Black
            )

            Text(text = movie.getGenres.joinToString(", "),
                Modifier
                    .constrainAs(genres_text) {
                        width = Dimension.fillToConstraints
                        absoluteLeft.linkTo(score_text.absoluteRight, 4.dp)
                        absoluteRight.linkTo(card_container.absoluteRight, 4.dp)
                    }
                    .padding(vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(
                    0x80000000
                ))
            Text(
                text = movie.overview ?: "",
                Modifier.constrainAs(overview_text) {
                    width = Dimension.fillToConstraints
                    absoluteLeft.linkTo(score_text.absoluteRight, 4.dp)
                    absoluteRight.linkTo(card_container.absoluteRight, 4.dp)
                },
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(
                    0x59000000
                )
            )
        }
    }
}

@Composable
fun LoadingItem() {
    CircularProgressIndicator(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .wrapContentWidth(
                Alignment.CenterHorizontally
            )
    )
}
