package com.example.dayvee.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomHorizontalDivider(
    modifier: Modifier = Modifier,
    topColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
    bottomColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
) {
    Column(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            topColor.copy(alpha = 0f),
                            topColor,
                            topColor.copy(alpha = 0.1f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            bottomColor.copy(alpha = 0f),
                            bottomColor,
                            bottomColor.copy(alpha = 0.2f)
                        )
                    )
                )
        )
    }
}
