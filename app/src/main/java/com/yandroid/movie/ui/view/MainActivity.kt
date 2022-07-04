package com.yandroid.movie.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yandroid.movie.ui.theme.MovieTheme
import com.yandroid.movie.ui.utils.BottomBarScreen
import com.yandroid.movie.ui.utils.NavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme {
                val navController = rememberNavController()
                val tabs = remember { BottomBarScreen.values() }
                Scaffold(

                    bottomBar = { BottomBar(navController = navController, tabs) },

                ) {
                    NavGraph(navController = navController,Modifier.padding(it))
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, screens: Array<BottomBarScreen>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: BottomBarScreen.Home

    val routes = remember { BottomBarScreen.values().map { it.route } }

    if (currentDestination in routes) {
        BottomNavigation(
            contentColor = Color.White,
            backgroundColor = Color(255, 255, 255, 255),
            modifier = Modifier.height(56.dp),
        ) {
            screens.forEach { screen ->
                CustomBottomNavigationItem(
                    label = screen.title,
                    icon = screen.icon,
                    selected = currentDestination == screen.route,
                    selectedBackgroundColor = screen.color,
                    unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RowScope.CustomBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    @DrawableRes icon: Int,
    label: String = "",
    selectedBackgroundColor: Color = Color(255, 255, 255, 51),
    selectedContentColor: Color = LocalContentColor.current,
    unselectedContentColor: Color = selectedContentColor.copy(alpha = ContentAlpha.medium)
) {

    val transition = updateTransition(targetState = selected, label = "")

    val background by transition.animateColor(label = "") {
        when (it) {
            false -> {
                Color(android.graphics.Color.TRANSPARENT)
            }
            true -> {
                selectedBackgroundColor.copy(alpha = ContentAlpha.disabled)
            }
        }
    }

    val foreground by transition.animateColor(label = "") {
        when (it) {
            false -> {
                selectedBackgroundColor.copy(alpha = ContentAlpha.disabled)
            }
            true -> {
                selectedBackgroundColor.copy(alpha = ContentAlpha.high)
            }
        }
    }


    BoxWithConstraints(
        Modifier
            .padding(horizontal = 8.dp, 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
            .background(background)
            .weight(1f)
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Icon(
                painter = painterResource(id = icon), contentDescription = "",
                tint = foreground
            )
            AnimatedVisibility(
                visible = selected,
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End),
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)

            ) {
                Text(
                    text = label, color = foreground, maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.offset(4.dp)
                )
            }
        }


    }
}