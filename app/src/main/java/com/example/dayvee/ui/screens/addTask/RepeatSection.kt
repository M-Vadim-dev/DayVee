package com.example.dayvee.ui.screens.addTask

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dayvee.R
import java.time.DayOfWeek
import java.util.Locale

enum class RepeatType {
    EVERYDAY, WEEKLY, MONTHLY, YEARLY
}

@Composable
fun RepeatSection() {
    var selectedDays by remember { mutableStateOf(emptyList<DayOfWeek>()) }
    var selectedRepeatType by remember { mutableStateOf(RepeatType.WEEKLY) }
    var isRepeatEnabled by remember { mutableStateOf(false) }

    val contentColor = if (isRepeatEnabled)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_repeat),
                contentDescription = null,
                tint = contentColor
            )
            Text(
                text = "Repeat",
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )

            Switch(
                checked = isRepeatEnabled,
                onCheckedChange = { isRepeatEnabled = it },
                modifier = Modifier.scale(0.75f)
            )
        }

        if (isRepeatEnabled) {
            RepeatOptionsSection(
                selectedDays = selectedDays,
                onDayToggle = { day ->
                    selectedDays = if (day in selectedDays) {
                        selectedDays - day
                    } else {
                        selectedDays + day
                    }
                },
                selectedRepeatType = selectedRepeatType,
                onRepeatTypeSelected = { selectedRepeatType = it }
            )
        }
    }
}

@Composable
fun RepeatDaysSelector(
    selectedDays: List<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit,
    locale: Locale = Locale.getDefault(),
) {
    val days = DayOfWeek.entries.let {
        it.subList(0, 6) + it[6]
    }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach { day ->
            val isSelected = day in selectedDays
            val label = day.getDisplayName(java.time.format.TextStyle.SHORT, locale)
                .replaceFirstChar { it.uppercase(locale) }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onDayToggle(day) }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun RepeatOptionsSection(
    selectedDays: List<DayOfWeek>,
    onDayToggle: (DayOfWeek) -> Unit,
    selectedRepeatType: RepeatType,
    onRepeatTypeSelected: (RepeatType) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RepeatTypeSelector(
            selectedRepeatType = selectedRepeatType, onRepeatTypeSelected = onRepeatTypeSelected
        )
        Text(
            text = "Repeat on",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
        )
        RepeatDaysSelector(
            selectedDays = selectedDays, onDayToggle = onDayToggle
        )
    }
}

@Composable
fun RepeatTypeSelector(
    selectedRepeatType: RepeatType,
    onRepeatTypeSelected: (RepeatType) -> Unit,
) {
    val types = listOf(
        RepeatType.EVERYDAY to "Everyday",
        RepeatType.WEEKLY to "Weekly",
        RepeatType.MONTHLY to "Monthly",
        RepeatType.YEARLY to "Yearly"
    )

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        types.forEach { (type, label) ->
            val selected = type == selectedRepeatType
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onRepeatTypeSelected(type) }
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center) {
                Text(
                    text = label,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

