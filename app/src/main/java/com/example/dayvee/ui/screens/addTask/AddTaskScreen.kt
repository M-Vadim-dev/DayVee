package com.example.dayvee.ui.screens.addTask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dayvee.R
import com.example.dayvee.domain.TaskValidationError
import com.example.dayvee.ui.components.CustomGradientButton

@Composable
fun AddTaskScreen(
    viewModel: AddTaskScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    val validationError by viewModel.validationErrorFlow.collectAsState(initial = null)

    val validationErrorString = validationError?.let { error ->
        when (error) {
            TaskValidationError.StartTimeInPast -> stringResource(R.string.error_start_time_in_past)
            TaskValidationError.EndTimeBeforeStart -> stringResource(R.string.error_end_time_before_start)
            TaskValidationError.InvalidStartTimeFormat -> stringResource(R.string.error_invalid_start_time_format)
            TaskValidationError.InvalidEndTimeFormat -> stringResource(R.string.error_invalid_end_time_format)
        }
    }

    LaunchedEffect(validationErrorString) {
        validationErrorString?.let { snackBarHostState.showSnackbar(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = uiState.title,
            onValueChange = viewModel::onTitleChange,
            shape = RoundedCornerShape(12.dp),
            label = { Text(text = "Title") },
            leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
            trailingIcon = {
                if (uiState.title.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onTitleChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = null)
                    }
                }
            },
            maxLines = 2,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.description,
            onValueChange = viewModel::onDescriptionChange,
            shape = RoundedCornerShape(12.dp),
            label = { Text(text = "Description") },
            leadingIcon = { Icon(Icons.Filled.Info, contentDescription = null) },
            trailingIcon = {
                if (uiState.description.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onDescriptionChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = null)
                    }
                }
            },
            maxLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.startHour,
                onValueChange = viewModel::onStartHourChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                label = { Text(text = "Hour", fontSize = 10.sp) },
                singleLine = true,
                isError = !uiState.isStartHourValid,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.startMinute,
                onValueChange = viewModel::onStartMinuteChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                label = { Text(text = "Minute", fontSize = 10.sp) },
                singleLine = true,
                isError = !uiState.isStartMinuteValid,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(0.5f))
            OutlinedTextField(
                value = uiState.endHour,
                onValueChange = viewModel::onEndHourChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                label = {
                    if (uiState.isEndHourValid) {
                        Text(text = "Hour", fontSize = 10.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                isError = !uiState.isEndHourValid,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = uiState.endMinute,
                onValueChange = viewModel::onEndMinuteChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                label = {
                    if (uiState.isEndMinuteValid) {
                        Text(text = "Minute", fontSize = 10.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                isError = !uiState.isEndMinuteValid,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomGradientButton(
            onClick = {
                viewModel.createTask()
                onDismiss()
            },
            enabled = uiState.isAddEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            text = stringResource(id = R.string.add_task),
        )
    }
}
