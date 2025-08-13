package com.example.dayvee.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dayvee.ui.screens.SettingsScreen
import com.example.dayvee.ui.screens.StatsScreen
import com.example.dayvee.ui.screens.intro.IntroScreen
import com.example.dayvee.ui.screens.main.MainScreen
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
    NavHost(navController, startDestination = Screen.Intro.route) {
        composable(Screen.Intro.route) { IntroScreen(navController) }
        composable(Screen.Main.route) { MainScreen(navController) }
        composable(Screen.Stats.route) { StatsScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(
            route = Screen.Task.route,
            arguments = listOf(navArgument("taskId") { type = NavType.IntType }),
        ) { TaskScreen(onBackClick = { navController.popBackStack() }) }
    }
}
