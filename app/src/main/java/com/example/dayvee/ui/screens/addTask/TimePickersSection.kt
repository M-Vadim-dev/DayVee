package com.example.dayvee.ui.screens.addTask

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.dayvee.R
import com.example.dayvee.ui.components.CustomCircularDigitalTimePicker

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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TimePickerColumn(
            iconRes = R.drawable.ic_schedule,
            labelRes = R.string.text_start_time_task,
            hour = startHour,
            minute = startMinute,
            onHourChange = onStartHourChange,
            onMinuteChange = onStartMinuteChange
        )
        Spacer(modifier = Modifier.weight(1f))
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
                tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f)
            )
            Text(
                text = stringResource(labelRes),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f),
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
