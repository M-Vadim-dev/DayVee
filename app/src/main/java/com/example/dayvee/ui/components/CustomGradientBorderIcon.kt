package com.example.dayvee.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.dayvee.ui.theme.GhostWhite
import com.example.dayvee.ui.theme.Gradients.verticalDarkPurpleGradient
import com.example.dayvee.ui.theme.Gradients.verticalPurpleGradient

@Composable
fun CustomGradientBorderIcon(
    modifier: Modifier = Modifier,
    borderWidth: Dp = 3.dp,
    size: Dp = 52.dp,
    gradientCircle: Brush,
    gradientBorder: Brush = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Transparent)
    ),
    iconSize: Dp = 32.dp,
    icon: ImageVector,
    iconTint: Color = GhostWhite,
    contentDescription: String? = null,
    rotation: Float = 0f,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = rotation
            }
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val strokeWidthPx = borderWidth.toPx()
            val radius = size.toPx() / 2f

            drawCircle(
                brush = gradientCircle,
                radius = size.toPx() / 2.2f,
                center = center
            )

            drawCircle(
                brush = gradientBorder,
                radius = radius - borderWidth.toPx() / 2f,
                style = Stroke(width = strokeWidthPx),
                center = center
            )
        }

        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize),
            tint = iconTint
        )
    }
}

@Preview
@Composable
private fun CustomGradientBorderIconPreview() {
    CustomGradientBorderIcon(
        gradientCircle = verticalPurpleGradient,
        gradientBorder = verticalDarkPurpleGradient,
        icon = Icons.Rounded.Add,
        onClick = {}
    )
}