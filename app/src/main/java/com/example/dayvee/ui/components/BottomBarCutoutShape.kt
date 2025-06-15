package com.example.dayvee.ui.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomBarCutoutShape(
    private val cornerRadius: Float,
    private val cutoutRadius: Float,
    private val cutoutCenterX: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()

        path.moveTo(0f, cornerRadius)

        path.arcTo(
            rect = Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        val cutoutLeft = cutoutCenterX - cutoutRadius
        val cutoutRight = cutoutCenterX + cutoutRadius

        path.lineTo(cutoutLeft - cornerRadius / 2f, 0f)

        path.quadraticTo(
            cutoutLeft, 0f,
            cutoutLeft, cornerRadius / 2f
        )

        path.cubicTo(
            cutoutLeft + cutoutRadius * 0.3f, cutoutRadius * 1.4f,
            cutoutRight - cutoutRadius * 0.3f, cutoutRadius * 1.4f,
            cutoutRight, cornerRadius / 2f
        )

        path.quadraticTo(
            cutoutRight, 0f,
            cutoutRight + cornerRadius / 2f, 0f
        )

        path.arcTo(
            rect = Rect(size.width - cornerRadius * 2, 0f, size.width, cornerRadius * 2),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )

        path.lineTo(size.width, size.height)
        path.lineTo(0f, size.height)
        path.close()

        return Outline.Generic(path)
    }
}