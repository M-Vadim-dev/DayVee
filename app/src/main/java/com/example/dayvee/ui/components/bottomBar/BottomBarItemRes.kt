package com.example.dayvee.ui.components.bottomBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomBarItemRes(
    val route: String,
    @StringRes val labelRes: Int,
    @DrawableRes val iconOutlinedRes: Int,
    @DrawableRes val iconFilledRes: Int,
)