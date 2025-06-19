package com.example.dayvee.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradients {
    val verticalPurpleGradient = Brush.verticalGradient(
        colors = listOf(MediumPurple, MediumOrchid)
    )

    val verticalDarkPurpleGradient = Brush.verticalGradient(
        colors = listOf(MediumPurple, DarkSlateGray)
    )

    val verticalMidnightBlueGradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, MidnightBlue)
    )

    val verticalBlackOverlayGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color.Black.copy(alpha = 0.12f),
            0.1f to Color.Black.copy(alpha = 0.06f),
            0.3f to Color.Transparent
        )
    )
}