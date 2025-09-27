package com.example.dayvee.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dayvee.ui.screens.StatsScreen
import com.example.dayvee.ui.screens.intro.IntroScreen
import com.example.dayvee.ui.screens.main.MainScreen
import com.example.dayvee.ui.screens.settings.SettingsScreen
import com.example.dayvee.ui.screens.task.TaskScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Main : Screen("main")
    object Task : Screen("task_detail/{taskId}") {
        fun createRoute(taskId: Int) = "task_detail/$taskId"
    }

    object Stats : Screen("stats")
    object Settings : Screen("settings")
}

@Composable
fun DayVeeNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Intro.route,
    ) {
        composable(route = Screen.Intro.route) { IntroScreen(navController) }

        composable(route = Screen.Main.route) { MainScreen(navController) }

        composable(
            route = Screen.Stats.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 300)
                )
            }
        ) { StatsScreen(navController) }

        composable(
            route = Screen.Settings.route,
            exitTransition = {
                if (targetState.destination.route == Screen.Main.route) {
                    slideOutHorizontally(
                        targetOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis = 300)
                    )
                } else null
            },
            popEnterTransition = {
                if (initialState.destination.route == Screen.Main.route) {
                    slideInHorizontally(
                        initialOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis = 300)
                    )
                } else null
            }
        ) { SettingsScreen(onBackClick = { navController.popBackStack() }) }

        composable(
            route = Screen.Task.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) { TaskScreen(onBackClick = { navController.popBackStack() }) }
    }
}
