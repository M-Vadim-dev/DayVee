package com.example.dayvee.ui.screens.stats

import android.content.res.Configuration
import android.graphics.Paint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.withSave
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dayvee.R
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.ui.extensions.toColor
import com.example.dayvee.ui.extensions.toDisplayName
import com.example.dayvee.ui.theme.CornflowerBlue
import com.example.dayvee.ui.theme.CriticalRed
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.HighOrange
import com.example.dayvee.ui.theme.LightSkyBlue
import com.example.dayvee.ui.theme.MediumOrchid
import com.example.dayvee.ui.theme.MediumPurple
import com.example.dayvee.ui.theme.SandyBrown
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null,
) {
    val stats by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.nav_stats),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_left),
                                contentDescription = stringResource(R.string.nav_back),
                                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        StatsScreenContent(
            stats = stats,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun StatsScreenContent(
    stats: TaskStats,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        MultiRingProgress(
            text = "${stringResource(R.string.total)}\n${stringResource(R.string.task)}",
            total = stats.total,
            completed = stats.completedPercent,
            inProgress = stats.inProgressPercent,
            pending = stats.pendingPercent,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))
        StatusLegend()

        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.task_progress),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                StatusProgressRow(stats = stats)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(R.string.tasks_by_priority),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                TagsList(stats.byPriority)
            }
        }
    }
}

@Composable
private fun MultiRingProgress(
    total: Int,
    completed: Float,
    inProgress: Float,
    pending: Float,
    text: String,
    modifier: Modifier = Modifier,
    size: Dp = 280.dp,
    sweepMax: Float = 280f,
) {
    val animatedCompleted = remember { Animatable(completed) }
    val animatedInProgress = remember { Animatable(inProgress) }
    val animatedPending = remember { Animatable(pending) }

    LaunchedEffect(completed, inProgress, pending) {
        launch { animatedPending.animateTo(pending, animationSpec = tween(1000)) }
        launch { animatedCompleted.animateTo(completed, animationSpec = tween(1000)) }
        launch { animatedInProgress.animateTo(inProgress, animationSpec = tween(1000)) }
    }

    val completedColors = listOf(CornflowerBlue, LightSkyBlue)
    val inProgressColors = listOf(SandyBrown, CriticalRed)
    val pendingColors = listOf(MediumOrchid, MediumPurple)
    val bgColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)

    Box(modifier = modifier.size(size)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.toPx() / 2
            val center = Offset(radius, radius)
            val strokeWidth = radius * 0.15f
            val gap = radius * 0.05f
            val startAngle = 90f

            fun drawRing(
                sweep: Float,
                colors: List<Color>,
                bgColor: Color,
                ringRadius: Float
            ) {
                drawArc(
                    color = bgColor,
                    startAngle = startAngle,
                    sweepAngle = sweepMax,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - ringRadius, center.y - ringRadius),
                    size = Size(ringRadius * 2, ringRadius * 2)
                )

                drawArc(
                    brush = Brush.horizontalGradient(
                        colors = colors,
                        startX = 0f,
                        endX = size.toPx()
                    ),
                    startAngle = startAngle,
                    sweepAngle = sweepMax * sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = Offset(center.x - ringRadius, center.y - ringRadius),
                    size = Size(ringRadius * 2, ringRadius * 2)
                )

                val sweepAngle = sweepMax * sweep

                val preSweepOffset = if (sweep == 0f) {
                    0f
                } else {
                    val minOffset = 16f
                    val maxOffset = 52f
                    minOffset + (size.toPx() / 2 - ringRadius) / (size.toPx()) * (maxOffset - minOffset)
                }

                val angleOffset =
                    Math.toDegrees((strokeWidth / 2) / ringRadius.toDouble()).toFloat()
                val angleText = startAngle + sweepAngle - angleOffset + preSweepOffset
                val rad = Math.toRadians(angleText.toDouble())

                val needsFlip = angleText in 170f..350f
                val rotation = if (sweep > 0f) {
                    var rot = angleText - 90f
                    if (angleText in 170f..350f) rot += 180f
                    rot
                } else 0f

                val textRadius = ringRadius + strokeWidth * 0.3f * if (needsFlip) -1 else 1
                val textX = (center.x + textRadius * cos(rad)).toFloat()
                val textY = (center.y + textRadius * sin(rad)).toFloat()

                drawContext.canvas.nativeCanvas.apply {
                    withSave {
                        translate(textX, textY)
                        rotate(rotation)
                        drawText(
                            "${(sweep * 100).toInt()}%",
                            0f,
                            0f,
                            Paint().apply {
                                textAlign = Paint.Align.CENTER
                                textSize = strokeWidth * 0.7f
                                color = android.graphics.Color.WHITE
                                isAntiAlias = true
                            }
                        )
                    }
                }

            }

            drawRing(
                sweep = animatedPending.value,
                colors = pendingColors,
                bgColor = bgColor,
                ringRadius = radius
            )
            val radius1 = radius - strokeWidth - gap
            drawRing(
                sweep = animatedCompleted.value,
                colors = completedColors,
                bgColor = bgColor,
                ringRadius = radius1
            )
            val radius2 = radius1 - strokeWidth - gap
            drawRing(
                sweep = animatedInProgress.value,
                colors = inProgressColors,
                bgColor = bgColor,
                ringRadius = radius2
            )

            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "$total",
                    center.x,
                    center.y + strokeWidth - 8f,
                    Paint().apply {
                        textAlign = Paint.Align.CENTER
                        textSize = radius / 3
                        color = android.graphics.Color.WHITE
                        isFakeBoldText = true
                        isAntiAlias = true
                    }
                )
            }
        }

        val legendFontSize = (size.value * 0.08).sp

        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = (size.value * 0.05).dp, bottom = (size.value * 0.03).dp),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = (legendFontSize.value * 1.2).sp,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            lineHeight = (legendFontSize.value * 1.3).sp
        )

    }
}

@Composable
private fun StatusLegend() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LegendRow(stringResource(R.string.stats_completed), LightSkyBlue)
        LegendRow(stringResource(R.string.stats_in_progress), SandyBrown)
        LegendRow(stringResource(R.string.stats_not_completed), MediumPurple)
    }
}

@Composable
private fun LegendRow(label: String, color: Color) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = MaterialTheme.shapes.extraSmall)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label, style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun StatusProgressRow(stats: TaskStats) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        StatusProgressItem(
            label = stringResource(R.string.stats_finished_tasks),
            count = stats.completedCount,
            ratio = stats.completedPercent,
            color1 = CornflowerBlue,
            color2 = LightSkyBlue
        )
        StatusProgressItem(
            label = stringResource(R.string.stats_in_progress),
            count = stats.inProgressCount,
            ratio = stats.inProgressPercent,
            color1 = SandyBrown,
            color2 = HighOrange
        )
        StatusProgressItem(
            label = stringResource(R.string.stats_unfinished_tasks),
            count = stats.pendingCount,
            ratio = stats.pendingPercent,
            color1 = MediumOrchid,
            color2 = MediumPurple
        )
    }
}

@Composable
private fun StatusProgressItem(
    label: String,
    count: Int,
    ratio: Float,
    color1: Color,
    color2: Color,
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "$count",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.1f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = animateFloatAsState(targetValue = ratio).value)
                    .background(brush = Brush.horizontalGradient(colors = listOf(color1, color2)))
            )
        }
    }
}

@Composable
private fun TagsList(byTag: Map<TaskPriority, Int>) {
    if (byTag.isEmpty()) {
        Text(
            text = stringResource(R.string.no_tasks),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        return
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        byTag.entries.sortedByDescending { it.value }.forEach { (priority, count) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = priority.toDisplayName(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = priority.toColor(),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatsScreenPreview() {
    val fakeStats = TaskStats(
        total = 11,
        completedPercent = 0.6f,
        inProgressPercent = 0.2f,
        pendingPercent = 0.3f,
        completedCount = 6,
        inProgressCount = 2,
        pendingCount = 3,
        byPriority = mapOf(
            TaskPriority.HIGH to 5,
            TaskPriority.MEDIUM to 10,
            TaskPriority.LOW to 4
        )
    )
    DayVeeTheme {
        StatsScreenContent(stats = fakeStats)
    }
}
