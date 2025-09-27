package com.example.dayvee.ui.screens.addTask

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dayvee.R
import com.example.dayvee.domain.TaskValidationError
import com.example.dayvee.domain.model.TaskIcon
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.ui.components.CustomGradientButton
import java.util.Locale

@Composable
fun AddTaskScreen(
    viewModel: AddTaskScreenViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState,
    onDismiss: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val validationError by viewModel.validationError.collectAsState(initial = null)
    val taskCreated by viewModel.taskCreated.collectAsState()

    val validationErrorString = validationError?.let { error ->
        when (error) {
            TaskValidationError.StartTimeInPast -> stringResource(R.string.error_start_time_in_past)
            TaskValidationError.EndTimeBeforeStart -> stringResource(R.string.error_end_time_before_start)
            TaskValidationError.InvalidStartTimeFormat -> stringResource(R.string.error_invalid_start_time_format)
            TaskValidationError.InvalidEndTimeFormat -> stringResource(R.string.error_invalid_end_time_format)
            TaskValidationError.InvalidStartHour -> stringResource(R.string.error_invalid_start_hour)
            TaskValidationError.InvalidStartMinute -> stringResource(R.string.error_invalid_start_minute)
            TaskValidationError.InvalidEndHour -> stringResource(R.string.error_invalid_end_hour)
            TaskValidationError.InvalidEndMinute -> stringResource(R.string.error_invalid_end_minute)
        }
    }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(validationErrorString) {
        validationErrorString?.let {
            focusManager.clearFocus()
            snackBarHostState.showSnackbar(it)
            viewModel.clearValidationError()
        }
    }

    LaunchedEffect(taskCreated) {
        if (taskCreated) {
            viewModel.acknowledgeTaskCreated()
            onDismiss()
        }
    }

    AddTaskScreenContent(
        uiState = uiState,
        onTitleChange = viewModel::onTitleChange,
        onPrioritySelected = viewModel::onPriorityChange,
        onIconSelected = { icon -> viewModel.onIconSelected(icon) },
        onDescriptionChange = viewModel::onDescriptionChange,
        onStartHourChange = viewModel::onStartHourChange,
        onStartMinuteChange = viewModel::onStartMinuteChange,
        onEndHourChange = viewModel::onEndHourChange,
        onEndMinuteChange = viewModel::onEndMinuteChange,
        onAddTaskClick = { viewModel.createTask() },
    )
}

@Composable
private fun AddTaskScreenContent(
    uiState: AddTaskScreenUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPrioritySelected: (TaskPriority) -> Unit,
    onIconSelected: (TaskIcon) -> Unit,
    onStartHourChange: (String) -> Unit,
    onStartMinuteChange: (String) -> Unit,
    onEndHourChange: (String) -> Unit,
    onEndMinuteChange: (String) -> Unit,
    onAddTaskClick: () -> Unit,
) {
    val now = java.time.LocalTime.now()

    val startHourInt: Int = uiState.startHour.toIntOrNull() ?: now.hour
    val startMinuteInt: Int = uiState.startMinute.toIntOrNull() ?: now.minute
    val endHourInt: Int = uiState.endHour.toIntOrNull() ?: now.hour
    val endMinuteInt: Int = uiState.endMinute.toIntOrNull() ?: now.minute

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitleSection(uiState.title, onTitleChange)
        Spacer(modifier = Modifier.height(12.dp))
        TaskCustomizationSection(
            selectedPriority = uiState.priority,
            onPrioritySelected = onPrioritySelected,
            selectedIcon = uiState.icon,
            onIconSelected = onIconSelected
        )
        Spacer(modifier = Modifier.height(12.dp))
        ManualTimeInputs(
            uiState,
            onStartHourChange,
            onStartMinuteChange,
            onEndHourChange,
            onEndMinuteChange
        )
        Spacer(modifier = Modifier.height(4.dp))
        TimePickersSection(
            startHour = startHourInt,
            startMinute = startMinuteInt,
            endHour = endHourInt,
            endMinute = endMinuteInt,
            onStartHourChange = onStartHourChange,
            onStartMinuteChange = onStartMinuteChange,
            onEndHourChange = onEndHourChange,
            onEndMinuteChange = onEndMinuteChange
        )
        Spacer(modifier = Modifier.height(4.dp))
        DescriptionSection(uiState.description, onDescriptionChange)
        Spacer(modifier = Modifier.height(8.dp))
        RepeatSection()
        Spacer(modifier = Modifier.height(16.dp))
        AddButtonSection(uiState.isAddEnabled, uiState.isEditMode, onAddTaskClick)
    }
}

@Composable
private fun TitleSection(title: String, onTitleChange: (String) -> Unit) {
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        shape = RoundedCornerShape(12.dp),
        label = { Text(text = stringResource(id = R.string.text_title_task)) },
        leadingIcon = { Icon(Icons.Filled.Edit, contentDescription = null) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        trailingIcon = {
            if (title.isNotEmpty()) {
                IconButton(onClick = { onTitleChange("") }) {
                    Icon(Icons.Filled.Clear, contentDescription = null)
                }
            }
        },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ManualTimeInputs(
    uiState: AddTaskScreenUiState,
    onStartHourChange: (String) -> Unit,
    onStartMinuteChange: (String) -> Unit,
    onEndHourChange: (String) -> Unit,
    onEndMinuteChange: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = uiState.startHour,
            onValueChange = onStartHourChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            label = { Text(text = stringResource(id = R.string.text_hour), fontSize = 10.sp) },
            singleLine = true,
            isError = !uiState.isStartHourValid,
            modifier = Modifier.weight(1f)
        )
        Text(text = ":", fontSize = 28.sp, modifier = Modifier.align(Alignment.CenterVertically))
        OutlinedTextField(
            value = uiState.startMinute,
            onValueChange = onStartMinuteChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            label = { Text(text = stringResource(id = R.string.text_minute), fontSize = 10.sp) },
            singleLine = true,
            isError = !uiState.isStartMinuteValid,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(0.4f))
        OutlinedTextField(
            value = uiState.endHour,
            onValueChange = onEndHourChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            label = {
                if (uiState.isEndHourValid) Text(
                    text = stringResource(id = R.string.text_hour),
                    fontSize = 10.sp
                )
                else Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            singleLine = true,
            isError = !uiState.isEndHourValid,
            modifier = Modifier.weight(1f)
        )
        Text(text = ":", fontSize = 28.sp, modifier = Modifier.align(Alignment.CenterVertically))
        OutlinedTextField(
            value = uiState.endMinute,
            onValueChange = onEndMinuteChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            label = {
                if (uiState.isEndMinuteValid) Text(
                    text = stringResource(id = R.string.text_minute),
                    fontSize = 10.sp
                )
                else Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            singleLine = true,
            isError = !uiState.isEndMinuteValid,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DescriptionSection(description: String, onDescriptionChange: (String) -> Unit) {
    VoiceInputTextField(
        value = description,
        onValueChange = onDescriptionChange,
        labelText = stringResource(R.string.text_description_task),
        leadingIcon = { Icon(painterResource(R.drawable.ic_info), contentDescription = null) }
    )
}

@Composable
private fun VoiceInputTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current

    val speechRecognizerIntent = remember(context) {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault().toLanguageTag()
            )
            putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                context.getString(R.string.voice_input_prompt)
            )
        }
    }

    val isSpeechAvailable = remember(context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.queryIntentActivities(
                speechRecognizerIntent,
                PackageManager.ResolveInfoFlags.of(0)
            ).isNotEmpty()
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.queryIntentActivities(
                speechRecognizerIntent,
                0
            ).isNotEmpty()
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                onValueChange(matches[0])
            }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(12.dp),
        label = { Text(text = labelText) },
        leadingIcon = leadingIcon,
        trailingIcon = {
            Row {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = null
                        )
                    }
                }
                if (isSpeechAvailable) {
                    IconButton(onClick = { speechLauncher.launch(speechRecognizerIntent) }) {
                        Icon(
                            painterResource(R.drawable.ic_mic),
                            contentDescription = null
                        )
                    }
                }
            }
        },
        maxLines = 3,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun AddButtonSection(
    isAddEnabled: Boolean,
    isEditMode: Boolean,
    onAddTaskClick: () -> Unit
) {
    CustomGradientButton(
        onClick = onAddTaskClick,
        enabled = isAddEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        text = if (isEditMode) stringResource(R.string.edit_task) else stringResource(R.string.add_task)
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TitleSectionPreview() {
    TitleSection(title = "Workout") {}
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ManualTimeInputsPreview() {
    ManualTimeInputs(
        uiState = AddTaskScreenUiState(
            title = "",
            description = "",
            startHour = "07",
            startMinute = "30",
            endHour = "08",
            endMinute = "15",
            isStartHourValid = true,
            isStartMinuteValid = true,
            isEndHourValid = true,
            isEndMinuteValid = true,
            isAddEnabled = true
        ),
        onStartHourChange = {},
        onStartMinuteChange = {},
        onEndHourChange = {},
        onEndMinuteChange = {}
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AddButtonSectionPreview() {
    AddButtonSection(isAddEnabled = true, isEditMode = false) {}
}
