package com.example.dayvee.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.dayvee.R
import com.example.dayvee.ui.theme.DarkSlateGray
import com.example.dayvee.ui.theme.GhostWhite
import com.example.dayvee.ui.theme.MediumOrchid
import com.example.dayvee.ui.theme.MediumPurple
import com.example.dayvee.ui.theme.MidnightBlue
import com.example.dayvee.ui.theme.Montserrat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun CustomDatePickerDialog(
    initialDate: LocalDate = LocalDate.now(),
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var currentMonth by remember { mutableStateOf(YearMonth.from(initialDate)) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MidnightBlue,
            tonalElevation = 6.dp
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Выберите дату",
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.DarkGray
                        )
                    }
                }

                CustomCalendar(
                    selectedDate = selectedDate,
                    currentMonth = currentMonth,
                    onDateChange = { date ->
                        selectedDate = date
                        currentMonth = YearMonth.from(date)  // Обновляем месяц при выборе даты
                        onDateSelected(date)  // Можно сразу сообщить о выборе
                        onDismiss()           // Закрыть диалог
                    },
                    onMonthChange = { month ->
                        currentMonth = month
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

            }
        }
    }
}

@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    currentMonth: YearMonth,
    onDateChange: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit
) {
    val locale = Locale.getDefault()
    val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek

    val days = remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { day -> currentMonth.atDay(day) }
    }

    val daysOfWeek = remember(locale) {
        DayOfWeek.entries.let { days ->
            val shift = days.indexOf(firstDayOfWeek)
            days.drop(shift) + days.take(shift)
        }
    }

    val offset = remember(currentMonth, firstDayOfWeek) {
        val firstDay = currentMonth.atDay(1).dayOfWeek
        val shift = (firstDay.ordinal - firstDayOfWeek.ordinal + 7) % 7
        List(shift) { null }
    }

    val paddedDays = offset + days

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        // Месяц и кнопки
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(MediumOrchid, MediumPurple)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        ) {
            IconButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    tint = Color.White
                )
            }
            Text(
                text = currentMonth.month.getDisplayName(TextStyle.FULL, locale)
                    .replaceFirstChar { it.uppercase() } + " ${currentMonth.year}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                color = Color.White
            )
            IconButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next",
                    tint = Color.White
                )
            }
        }

        // Заголовки дней недели
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            daysOfWeek.forEach { dayOfWeek ->
                Text(
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                        .replaceFirstChar { it.uppercase() },
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    fontSize = 14.sp,
                )
            }
        }
        HorizontalDivider(
            color = Color.Gray,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            paddedDays.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    (0 until 7).forEach { index ->
                        val day = week.getOrNull(index)
                        if (day != null) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (day == selectedDate) MediumPurple else DarkSlateGray)
                                    .clickable { onDateChange(day) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = day.dayOfMonth.toString(),
                                    fontSize = 16.sp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (day == selectedDate) GhostWhite else Color.White
                                )
                            }
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomCalendarPreview() {
    MaterialTheme {
        Surface(color = MidnightBlue) {
            CustomCalendar(
                selectedDate = LocalDate.now(),
                currentMonth = YearMonth.now(),
                onDateChange = {},
                onMonthChange = {}
            )
        }
    }
}


@Composable
fun CustomDatePicker(
    selectedDate: LocalDate,
//    isSelected: Boolean,
    hasTasks: Boolean = true,
    onDateSelected: (LocalDate) -> Unit,
) {
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scaleAnimation"
    )

    val daysInMonth = remember(currentMonth) {
        (1..currentMonth.lengthOfMonth()).map { day ->
            currentMonth.atDay(day)
        }
    }

    val formattedDate = remember(selectedDate) {
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM", Locale.getDefault())
        selectedDate.format(formatter).replaceFirstChar { it.uppercaseChar() }
    }

    var showCustomPicker by remember { mutableStateOf(false) }

    if (showCustomPicker) {
        CustomDatePickerDialog(
            initialDate = selectedDate,
            onDateSelected = {
                onDateSelected(it)
                currentMonth = YearMonth.from(it)
                showCustomPicker = false
            },
            onDismiss = { showCustomPicker = false },
        )
    }

    val context2 = LocalDensity.current
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate, currentMonth) {
        val index = daysInMonth.indexOf(selectedDate)
        if (index != -1) {
            val visibleItemWidth = 42.dp + 12.dp
            val centerOffset = with(context2) {
                ((listState.layoutInfo.viewportEndOffset - visibleItemWidth.toPx()) / 2).toInt()
            }
            listState.animateScrollToItem(index, scrollOffset = -centerOffset)
        }
    }

    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = stringResource(R.string.today),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 18.dp)
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
                    showCustomPicker = true
                }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = "Open Calendar",
                tint = GhostWhite,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = GhostWhite
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(
                daysInMonth, key = { it.toEpochDay() }) { date ->

                val isSelected = date == selectedDate

                val scale by animateFloatAsState(
                    targetValue = if (isSelected) 1.2f else 1f,
                    animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
                    label = "ScaleAnimation"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                                        brush = Brush.verticalGradient(
                                            colors = listOf(MediumPurple, MediumOrchid)
                                        ), cornerRadius = CornerRadius(8.dp.toPx())
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
                        .padding(8.dp)) {
                    Text(
                        text = date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT, Locale.getDefault()
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) GhostWhite else Color.Gray
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isSelected) GhostWhite else GhostWhite,
                        fontWeight = FontWeight.Bold
                    )
//                    if (isSelected) {
//                        HorizontalDivider(
//                            color = GhostWhite,
//                            thickness = 2.dp,
//                            modifier = Modifier.padding(3.dp)
//                        )
//                    }
//                    if (hasTasks) {
//                        Spacer(modifier = Modifier.height(4.dp))
//                        Icon(
//                            imageVector = Icons.Outlined.CheckCircle,
//                            contentDescription = "Tasks indicator",
//                            tint = if (isSelected) GhostWhite else MediumOrchid,
//                            modifier = Modifier.size(12.dp)
//                        )
//                    }
                }
            }
        }
    }
}