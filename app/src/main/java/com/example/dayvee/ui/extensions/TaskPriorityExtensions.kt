package com.example.dayvee.ui.extensions

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.dayvee.R
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.ui.theme.CriticalRed
import com.example.dayvee.ui.theme.CustomGreen
import com.example.dayvee.ui.theme.HighOrange
import com.example.dayvee.ui.theme.MediumYellow
import com.example.dayvee.ui.theme.MinorBlue
import com.example.dayvee.ui.theme.NoneTransparent

@Composable
fun TaskPriority.toDisplayName(): String = when (this) {
    TaskPriority.CRITICAL -> stringResource(R.string.priority_critical)
    TaskPriority.URGENT -> stringResource(R.string.priority_urgent)
    TaskPriority.HIGH -> stringResource(R.string.priority_high)
    TaskPriority.MEDIUM -> stringResource(R.string.priority_medium)
    TaskPriority.LOW -> stringResource(R.string.priority_low)
    TaskPriority.MINOR -> stringResource(R.string.priority_minor)
    TaskPriority.OPTIONAL -> stringResource(R.string.priority_optional)
    TaskPriority.CUSTOM -> stringResource(R.string.priority_custom)
    TaskPriority.NONE -> stringResource(R.string.priority_none)
}

@Composable
fun TaskPriority.toColor(): Color = when (this) {
    TaskPriority.CRITICAL -> CriticalRed
    TaskPriority.URGENT -> HighOrange
    TaskPriority.HIGH -> MediumYellow
    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.primary
    TaskPriority.LOW -> MaterialTheme.colorScheme.tertiary
    TaskPriority.MINOR -> MinorBlue
    TaskPriority.OPTIONAL -> MaterialTheme.colorScheme.onPrimary
    TaskPriority.CUSTOM -> CustomGreen
    TaskPriority.NONE -> NoneTransparent
}