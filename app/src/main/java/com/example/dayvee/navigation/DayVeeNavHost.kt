package com.example.dayvee.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dayvee.ui.screens.IntroScreen
import com.example.dayvee.ui.screens.home.AddTaskScreen
import com.example.dayvee.ui.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Home : Screen("home")
    object Task : Screen("task")
    object Calendar : Screen("calendar")
    object Habits : Screen("habits")
    object Stats : Screen("stats")
}

@Composable
fun DayVeeNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Intro.route) { IntroScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Task.route) {
            AddTaskScreen(
                navController = navController,
                onDismiss = { }
            )
        }
//        composable(Screen.Calendar.route) { CalendarScreen(navController) }
//        composable(Screen.Habits.route) { HabitsScreen(navController) }
//        composable(Screen.Stats.route) { StatsScreen(navController) }
    }
}
