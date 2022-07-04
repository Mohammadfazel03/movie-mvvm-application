package com.yandroid.movie.ui.utils

import androidx.annotation.DrawableRes
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yandroid.movie.R
import com.yandroid.movie.ui.theme.PrimaryColor
import com.yandroid.movie.ui.theme.SecondaryColor
import com.yandroid.movie.ui.theme.TriadicColor
import com.yandroid.movie.ui.view.Category.CategoryScreen
import com.yandroid.movie.ui.view.Home.HomeScreen
import com.yandroid.movie.ui.view.ListMovies.ListMovieScreen
import com.yandroid.movie.ui.view.Movie.MovieScreen
import com.yandroid.movie.ui.view.Search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController = navController, startDestination = BottomBarScreen.Home.route) {
        composable(BottomBarScreen.Home.route) {
            HomeScreen(modifier, navController)
        }
        composable(BottomBarScreen.Genres.route) {
            CategoryScreen(modifier, navController)
        }
        composable(BottomBarScreen.Search.route) {
            SearchScreen(modifier, navController)
        }
        composable("movie/{movieId}", arguments = listOf(navArgument("movieId") {
            type = NavType.IntType
            defaultValue = -1
        })) {
            MovieScreen(it.arguments!!.getInt("movieId"),navController)
        }
        composable("listMovie/{type}?id={id}", arguments = listOf(navArgument("id") {
            type = NavType.IntType
            defaultValue = -1
        })) {
            ListMovieScreen(it.arguments!!.getString("type")!!,it.arguments!!.getInt("id"),navController)
        }
    }

}


enum class BottomBarScreen(
    val route: String,
    val title: String,
    val color: Color,
    @DrawableRes val icon: Int
) {
    Home(
        route = "home",
        title = "Home",
        color = PrimaryColor,
        icon = R.drawable.ic_movie
    ),

    Genres(
        route = "genre",
        title = "Category",
        color = TriadicColor,
        icon = R.drawable.ic_category
    ),

    Search(
        route = "search",
        title = "Search",
        color = SecondaryColor,
        icon = R.drawable.ic_search
    )
}

