package com.example.dayvee.ui.components.bottomBar

import com.example.dayvee.R
import com.example.dayvee.navigation.Screen

val bottomBarItems = listOf(
    BottomBarItemRes(
        route = Screen.Main.route,
        labelRes = R.string.nav_home,
        iconOutlinedRes = R.drawable.ic_home,
        iconFilledRes = R.drawable.ic_home_fill
    ),
    BottomBarItemRes(
        route = Screen.Intro.route,
        labelRes = R.string.nav_calendar,
        iconOutlinedRes = R.drawable.ic_assignment ,
        iconFilledRes = R.drawable.ic_assignment_fill
    ),
    BottomBarItemRes(
        route = Screen.Stats.route,
        labelRes = R.string.nav_stats,
        iconFilledRes = R.drawable.ic_bar_chart ,
        iconOutlinedRes = R.drawable.ic_bar_chart_line
    ),
    BottomBarItemRes(
        route = Screen.Settings.route,
        labelRes = R.string.nav_setting,
        iconFilledRes = R.drawable.ic_settings,
        iconOutlinedRes = R.drawable.ic_settings_fill
    )
)