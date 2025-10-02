package com.example.dayvee.ui.screens.addTask

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dayvee.R
import com.example.dayvee.ui.components.CustomCircularDigitalTimePicker
import com.example.dayvee.ui.theme.DayVeeTheme

@Composable
fun TimePickersSection(
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    onStartHourChange: (String) -> Unit,
    onStartMinuteChange: (String) -> Unit,
    onEndHourChange: (String) -> Unit,
    onEndMinuteChange: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            )
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TimePickerColumn(
                iconRes = R.drawable.ic_schedule,
                labelRes = R.string.text_start_time_task,
                hour = startHour,
                minute = startMinute,
                onHourChange = onStartHourChange,
                onMinuteChange = onStartMinuteChange
            )
            Spacer(modifier = Modifier.width(68.dp))
            TimePickerColumn(
                iconRes = R.drawable.ic_pace,
                labelRes = R.string.text_end_time_task,
                hour = endHour,
                minute = endMinute,
                onHourChange = onEndHourChange,
                onMinuteChange = onEndMinuteChange
            )
        }
    }
}

@Composable
private fun TimePickerColumn(
    iconRes: Int,
    labelRes: Int,
    hour: Int,
    minute: Int,
    onHourChange: (String) -> Unit,
    onMinuteChange: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        CustomCircularDigitalTimePicker(
            hour = hour,
            minute = minute,
            onHourChange = { onHourChange(it.toString().padStart(2, '0')) },
            onMinuteChange = { onMinuteChange(it.toString().padStart(2, '0')) },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TimePickersSectionPreview() {
    DayVeeTheme {
        TimePickersSection(
            startHour = 7,
            startMinute = 30,
            endHour = 8,
            endMinute = 15,
            onStartHourChange = {},
            onStartMinuteChange = {},
            onEndHourChange = {},
            onEndMinuteChange = {}
        )
    }
}
