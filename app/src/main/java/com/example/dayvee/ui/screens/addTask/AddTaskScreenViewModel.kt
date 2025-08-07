package com.example.dayvee.ui.screens.addTask

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.managers.TaskStartAlarmManager
import com.example.dayvee.data.repository.SharedDateRepository
import com.example.dayvee.domain.TaskValidationError
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.User
import com.example.dayvee.domain.repository.TaskRepository
import com.example.dayvee.domain.repository.UserRepository
import com.example.dayvee.utils.TimeUtils.convertToMillis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@Immutable
data class AddTaskScreenUiState(
    val activeUser: User? = null,
    val today: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: YearMonth = YearMonth.from(LocalDate.now()),

    val title: String = "",
    val description: String = "",
    val startHour: String = "",
    val startMinute: String = "",
    val endHour: String = "",
    val endMinute: String = "",

    val isStartHourValid: Boolean = true,
    val isStartMinuteValid: Boolean = true,
    val isEndHourValid: Boolean = true,
    val isEndMinuteValid: Boolean = true,

    val isAddEnabled: Boolean = false,
)

@HiltViewModel
class AddTaskScreenViewModel @Inject constructor(
    private val sharedDateRepository: SharedDateRepository,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val taskStartAlarmManager: TaskStartAlarmManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTaskScreenUiState())
    val uiState: StateFlow<AddTaskScreenUiState> = _uiState.asStateFlow()

    private val _validationError = MutableStateFlow<TaskValidationError?>(null)
    val validationError: StateFlow<TaskValidationError?> = _validationError.asStateFlow()

    private val _taskCreated = MutableStateFlow(false)
    val taskCreated: StateFlow<Boolean> = _taskCreated.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(activeUser = userRepository.getActiveUser()) }
            sharedDateRepository.selectedDate
                .collect { date ->
                    _uiState.update { it.copy(selectedDate = date) }
                }
        }
    }

    internal fun createTask() {
        viewModelScope.launch {
            val state = uiState.value

            val user = state.activeUser ?: return@launch

            val title = state.title.trim()
            val description = state.description.trim()
            val startHour = state.startHour
            val startMinute = state.startMinute
            val endHour = state.endHour
            val endMinute = state.endMinute

            val selectedDate = state.selectedDate
            val startMillis = convertToMillis(selectedDate, startHour, startMinute)
            val endMillis = convertToMillis(selectedDate, endHour, endMinute)
            val nowDate = LocalDate.now()
            val nowMillis = System.currentTimeMillis()

            val isDone = when {
                selectedDate.isBefore(nowDate) -> true
                selectedDate.isEqual(nowDate) && endMillis < nowMillis -> true
                else -> false
            }

            if (selectedDate.isEqual(LocalDate.now()) && startMillis < nowMillis) {
                _validationError.value = TaskValidationError.StartTimeInPast
                return@launch
            }

            if (startMillis > endMillis) {
                _validationError.value = TaskValidationError.EndTimeBeforeStart
                return@launch
            }

            val newTask = Task(
                userId = user.uid,
                title = title,
                description = description,
                date = selectedDate,
                startTime = startMillis,
                endTime = endMillis,
                isDone = isDone
            )

            val taskId = taskRepository.addTask(newTask)
            _taskCreated.value = true

            taskStartAlarmManager.scheduleTaskStartAlarm(
                taskId = taskId,
                taskTitle = newTask.title,
                startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startMillis), ZoneId.systemDefault())
            )

            _uiState.update {
                it.copy(
                    title = "",
                    description = "",
                    startHour = "",
                    startMinute = "",
                    endHour = "",
                    endMinute = ""
                )
            }
        }
    }

    internal fun clearValidationError() {
        _validationError.value = null
    }

    internal fun acknowledgeTaskCreated() {
        _taskCreated.value = false
    }

    internal fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
        clearValidationError()
        validate(_uiState.value.copy(title = title))
    }

    internal fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
        clearValidationError()
        validate(_uiState.value.copy(description = description))
    }

    internal fun onStartHourChange(newValue: String) {
        if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
            val hourInt = newValue.toIntOrNull()
            val isValid = hourInt != null && hourInt in 0..23
            _uiState.update { it.copy(startHour = newValue, isStartHourValid = isValid) }
            validate(_uiState.value)
        }
    }

    internal fun onStartMinuteChange(newValue: String) {
        if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
            val minuteInt = newValue.toIntOrNull()
            val isValid = minuteInt != null && minuteInt in 0..59
            _uiState.update { it.copy(startMinute = newValue, isStartMinuteValid = isValid) }
            validate(_uiState.value)
        }
    }

    internal fun onEndHourChange(newValue: String) {
        if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
            val hourInt = newValue.toIntOrNull()
            val isValid = hourInt != null && hourInt in 0..23
            _uiState.update { it.copy(endHour = newValue, isEndHourValid = isValid) }
            validate(_uiState.value)
        }
    }

    internal fun onEndMinuteChange(newValue: String) {
        if (newValue.length <= 2 && newValue.all { it.isDigit() }) {
            val minuteInt = newValue.toIntOrNull()
            val isValid = minuteInt != null && minuteInt in 0..59
            _uiState.update { it.copy(endMinute = newValue, isEndMinuteValid = isValid) }
            validate(_uiState.value)
        }
    }

    private fun validate(state: AddTaskScreenUiState) {
        val titleValid = state.title.isNotBlank()
        val startHourValid = state.isStartHourValid && state.startHour.isNotBlank()
        val startMinuteValid = state.isStartMinuteValid && state.startMinute.isNotBlank()
        val endHourValid = state.isEndHourValid && state.endHour.isNotBlank()
        val endMinuteValid = state.isEndMinuteValid && state.endMinute.isNotBlank()

        val allFieldsFilled = titleValid &&
                startHourValid && startMinuteValid &&
                endHourValid && endMinuteValid

        _uiState.update { it.copy(isAddEnabled = allFieldsFilled) }
    }

}
