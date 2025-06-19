package com.example.dayvee.ui.animations

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable

@Composable
fun rememberItemSelectedScale(
    isSelected: Boolean,
    selectedScale: Float = 1.2f,
    defaultScale: Float = 1f,
    durationMillis: Int = 300
): Float {
    return animateFloatAsState(
        targetValue = if (isSelected) selectedScale else defaultScale,
        animationSpec = tween(durationMillis = durationMillis, easing = FastOutSlowInEasing)
    ).value
}
