package com.example.dayvee.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dayvee.ui.screens.home.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calendar : Screen("calendar")
    object Habits : Screen("habits")
    object Stats : Screen("stats")
}

@Composable
fun DayVeeNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { HomeScreen() }
//        composable(Screen.Calendar.route) { CalendarScreen() }
//        composable(Screen.Habits.route) { HabitsScreen() }
//        composable(Screen.Stats.route) { StatsScreen() }
    }
}
