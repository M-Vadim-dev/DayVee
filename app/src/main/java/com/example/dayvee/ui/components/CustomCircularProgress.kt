package com.example.dayvee.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dayvee.ui.theme.MediumOrchid
import com.example.dayvee.ui.theme.MediumPurple

@Composable
fun CustomCircularProgress(
    modifier: Modifier,
    progress: Float,
    size: Dp = 46.dp,
    strokeWidth: Dp = 4.dp,
    progressColors: List<Color> = listOf(MediumOrchid, MediumPurple),
    backgroundColor: Color = Color.Gray.copy(alpha = 0.3f),
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f)
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size - strokeWidth)) {
            val stroke = strokeWidth.toPx()
            val canvasSize = size.toPx() - stroke
            val radius = canvasSize / 2f

            drawCircle(
                color = backgroundColor,
                radius = radius,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke)
            )

            val brush = Brush.horizontalGradient(
                colors = progressColors,
                startX = 0f,
                endX = canvasSize
            )

            drawArc(
                brush = brush,
                startAngle = -90f,
                sweepAngle = 360 * animatedProgress,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke),
                size = androidx.compose.ui.geometry.Size(canvasSize, canvasSize),
                topLeft = Offset(0f, 0f)
            )
        }

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.29f).sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}