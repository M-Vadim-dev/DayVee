package com.example.dayvee.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dayvee.ui.theme.DayVeeTheme

@Composable
fun CustomCircularDigitalTimePicker(
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularTimePickerColumn(
            range = 24,
            initialValue = hour,
            onValueChange = onHourChange,
            modifier = Modifier.width(48.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = ":",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(4.dp))
        CircularTimePickerColumn(
            range = 60,
            initialValue = minute,
            onValueChange = onMinuteChange,
            modifier = Modifier.width(48.dp)
        )
    }
}

@Composable
private fun GradientFadingLazyColumn(
    state: LazyListState,
    itemsCount: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    val density = LocalDensity.current
    val gradientHeight = 48.dp
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .drawWithContent {
                drawContent()
                val heightPx = with(density) { gradientHeight.toPx() }
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = 0.8f),
                            backgroundColor.copy(alpha = 0f)
                        ),
                        startY = 0f,
                        endY = heightPx
                    ),
                    size = Size(size.width, heightPx)
                )
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            backgroundColor.copy(alpha = 0f),
                            backgroundColor.copy(alpha = 0.8f)
                        ),
                        startY = size.height - heightPx,
                        endY = size.height
                    ),
                    topLeft = Offset(0f, size.height - heightPx),
                    size = Size(size.width, heightPx)
                )
            }
    ) {
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item { Spacer(modifier = Modifier.height(gradientHeight)) }
            items(itemsCount) { index -> content(index) }
            item { Spacer(modifier = Modifier.height(gradientHeight)) }
        }
    }
}

@Composable
private fun CircularTimePickerColumn(
    range: Int,
    initialValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 30.dp,
    visibleItemCount: Int = 3,
) {
    val repeatCount = 1000
    val listSize = range * repeatCount
    val initialIndex = range * (repeatCount / 2) + initialValue

    val state = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)

    LaunchedEffect(state) {
        snapshotFlow { getCenteredIndex(state) }
            .collect { centerIndex ->
                if (centerIndex != null) {
                    val value = centerIndex % range
                    onValueChange(value)
                }
            }
    }

    GradientFadingLazyColumn(
        state = state,
        itemsCount = listSize,
        modifier = modifier.height(itemHeight * visibleItemCount)
    ) { index ->
        val value = index % range
        val isSelected = getCenteredIndex(state) == index
        Text(
            text = value.toString().padStart(2, '0'),
            fontSize = if (isSelected) 24.sp else 18.sp,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier
                .height(itemHeight)
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )

    }

}

private fun getCenteredIndex(state: LazyListState): Int? {
    val viewportHeight = state.layoutInfo.viewportSize.height
    if (viewportHeight == 0) return null

    val centerY = viewportHeight / 4f

    return state.layoutInfo.visibleItemsInfo.minByOrNull {
        kotlin.math.abs((it.offset + it.size / 2f) - centerY)
    }?.index
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomCircularDigitalTimePickerPreview() {
    val hourState = remember { mutableIntStateOf(12) }
    val minuteState = remember { mutableIntStateOf(30) }

    DayVeeTheme {
        CustomCircularDigitalTimePicker(
            hour = hourState.intValue,
            minute = minuteState.intValue,
            onHourChange = { hourState.intValue = it },
            onMinuteChange = { minuteState.intValue = it },
            modifier = Modifier
                .width(160.dp)
                .height(160.dp)
        )
    }
}
