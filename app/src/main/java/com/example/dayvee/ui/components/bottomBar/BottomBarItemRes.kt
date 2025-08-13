package com.example.dayvee.ui.components.bottomBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomBarItemRes(
    val route: String,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconOutlinedRes: Int,
    @param:DrawableRes val iconFilledRes: Int,
)