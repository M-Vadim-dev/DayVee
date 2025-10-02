package com.example.dayvee.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dayvee.R
import com.example.dayvee.ui.animations.rememberItemSelectedScale
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.GhostWhite
import com.example.dayvee.ui.theme.Gradients.verticalPurpleGradient
import com.example.dayvee.utils.DateUtils
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CustomDatePicker(
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    isDatePickerVisible: Boolean,
    isToday: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit,
    onShowPicker: () -> Unit,
    onDismissPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f)

    val daysInMonth = remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { day -> currentMonth.atDay(day) }
    }

    val formattedDate = remember(selectedDate) { DateUtils.formatFullDate(selectedDate) }

    if (isDatePickerVisible) {
        CustomDatePickerDialog(
            selectedDate = selectedDate,
            currentMonth = currentMonth,
            onDateSelected = {
                onDateSelected(it)
                onMonthChange(YearMonth.from(it))
                onDismissPicker()
            },
            onDismiss = onDismissPicker,
            onMonthChange = onMonthChange
        )
    }

    val density = LocalDensity.current
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate, currentMonth) {
        val index = daysInMonth.indexOf(selectedDate)
        if (index != -1) {
            val visibleItemWidth = 42.dp + 12.dp
            val centerOffset = with(density) {
                ((listState.layoutInfo.viewportEndOffset - visibleItemWidth.toPx()) / 2).toInt()
            }
            listState.animateScrollToItem(index, scrollOffset = -centerOffset)
        }
    }

    Column(modifier = modifier.padding(bottom = 10.dp)) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.titleSmall,
            color = if (isToday) MaterialTheme.colorScheme.onSurfaceVariant
            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = 18.dp)
                .clickable {
                    val today = LocalDate.now()
                    onDateSelected(today)
                    onMonthChange(YearMonth.from(today))
                }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .wrapContentWidth()
                .padding(start = 20.dp, top = 6.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onShowPicker()
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysInMonth, key = { it.toEpochDay() }) { date ->

                val isSelected = date == selectedDate
                val scale = rememberItemSelectedScale(isSelected)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .clip(RoundedCornerShape(8.dp))
                        .then(
                            if (isSelected) {
                                Modifier.drawBehind {
                                    drawRoundRect(
                                        brush = verticalPurpleGradient,
                                        cornerRadius = CornerRadius(8.dp.toPx())
                                    )
                                }
                            } else {
                                Modifier.background(
                                    Color.Transparent, shape = RoundedCornerShape(8.dp)
                                )
                            })
                        .clickable { onDateSelected(date) }
                        .width(42.dp)
                        .height(55.dp)
                ) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT, Locale.getDefault()
                        ),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) GhostWhite
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (!isSelected) Spacer(modifier = Modifier.padding(2.dp))

                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = if (isSelected) GhostWhite
                        else MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )

                    if (isToday && isSelected) {
                        HorizontalDivider(
                            color = GhostWhite,
                            thickness = 2.dp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp)
                        )
//                        Box(
//                            modifier = Modifier
//                                .padding(8.dp)
//                                .size(6.dp)
//                                .aspectRatio(1f)
//                                .background(
//                                    color = MaterialTheme.colorScheme.primary,
//                                    shape = CircleShape
//                                )
//                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CustomDatePickerPreview() {
    val today = LocalDate.now()
    val currentMonth = YearMonth.from(today)

    var selectedDate by remember { mutableStateOf(today) }
    var isVisible by remember { mutableStateOf(false) }

    DayVeeTheme {
        CustomDatePicker(
            selectedDate = selectedDate,
            currentMonth = currentMonth,
            isDatePickerVisible = isVisible,
            isToday = true,
            onDateSelected = { selectedDate = it },
            onMonthChange = {},
            onShowPicker = { isVisible = true },
            onDismissPicker = { isVisible = false },
        )
    }
}