package com.example.dayvee.ui.screens.main

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.dayvee.R
import com.example.dayvee.domain.model.TaskIcon
import com.example.dayvee.ui.theme.CriticalRed
import com.example.dayvee.ui.theme.DayVeeTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeTaskItem(
    modifier: Modifier = Modifier,
    textTitle: String,
    textDescription: String,
    priorityColor: Color,
    icon: TaskIcon?,
    timeStart: String,
    timeEnd: String,
    isComplete: Boolean,
    progress: Float,
    onClick: () -> Unit = {},
    onProgressLongClick: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    val screenWidthPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }
    val offsetX = remember { Animatable(0f) }

    val maxOffset = screenWidthPx * 0.4f
    val maxEditWidth = screenWidthPx * 0.6f
    val maxDeleteWidth = screenWidthPx * 0.2f

    val fraction = (-offsetX.value / maxOffset).coerceIn(0f, 1f)

    val editWidthPx = lerp(0f, maxEditWidth * 1.2f, fraction * 0.4f)
    val deleteWidthPx = lerp(0f, maxDeleteWidth * 0.8f, fraction)


    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .clip(MaterialTheme.shapes.medium)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier.width(with(LocalDensity.current) { maxEditWidth.toDp() })
            ) {
                Box(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { editWidthPx.toDp() })
                        .background(MaterialTheme.colorScheme.tertiary)
                        .align(Alignment.CenterEnd)
                        .clickable {
                            scope.launch {
                                onEdit()
                                offsetX.animateTo(0f, animationSpec = tween(durationMillis = 200))
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.icon_edit),
                            contentDescription = stringResource(R.string.edit),
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                        Text(
                            text = stringResource(R.string.edit),
                            fontSize = 10.sp,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(with(LocalDensity.current) { deleteWidthPx.toDp() })
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        scope.launch {
                            onDelete()
                            offsetX.animateTo(0f, animationSpec = tween(durationMillis = 200))
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_trash),
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = stringResource(R.string.delete),
                        fontSize = 10.sp,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            val newOffset = (offsetX.value + dragAmount).coerceIn(-maxOffset, 0f)
                            change.consume()
                            scope.launch {
                                offsetX.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            val threshold = maxOffset * 0.3f
                            val target = if (offsetX.value > -threshold) 0f else -maxOffset

                            scope.launch {
                                offsetX.animateTo(
                                    target,
                                    animationSpec = tween(durationMillis = 200)
                                )
                            }
                        }
                    )
                }
                .clickable(enabled = offsetX.value == 0f, onClick = onClick)
        ) {
            TaskItem(
                textTitle = textTitle,
                textDescription = textDescription,
                iconPainter = when (icon) {
                    is TaskIcon.Resource -> painterResource(id = icon.resId)
                    is TaskIcon.Custom -> null // todo
                    is TaskIcon.Default -> painterResource(id = R.drawable.ic_assignment)
                    null -> null
                },
                imageVector = if (icon == null) ImageVector.vectorResource(R.drawable.ic_account_circle) else null,
                colorLabel = priorityColor,
                isCompleted = isComplete,
                progress = progress,
                onProgressClick = onProgressLongClick,
                timeStart = timeStart,
                timeEnd = timeEnd
            )

            if (isComplete) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SwipeTaskItemActivePreview() {
    DayVeeTheme {
        Column {
            SwipeTaskItem(
                textTitle = "Prepare presentation",
                textDescription = "Slides for Monday meeting",
                priorityColor = CriticalRed,
                icon = TaskIcon.Resource(R.drawable.ic_assignment),
                timeStart = "13:00",
                timeEnd = "13:30",
                isComplete = true,
                progress = 1f,
                modifier = Modifier.height(90.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            SwipeTaskItem(
                textTitle = "Prepare presentation",
                textDescription = "Slides for Monday meeting",
                priorityColor = CriticalRed,
                icon = TaskIcon.Resource(R.drawable.ic_assignment),
                timeStart = "14:00",
                timeEnd = "15:30",
                isComplete = false,
                progress = 0.35f,
                modifier = Modifier.height(90.dp)
            )

        }
    }
}
