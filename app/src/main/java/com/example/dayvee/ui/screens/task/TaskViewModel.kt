package com.example.dayvee.ui.screens.task

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.domain.formatter.DurationFormatter
import com.example.dayvee.domain.repository.TaskRepository
import com.example.dayvee.utils.DateUtils.formatDate
import com.example.dayvee.utils.DateUtils.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@Immutable
data class TaskUiState(
    val title: String = "",
    val description: String = "",
    val totalDuration: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val date: String = "",
    val reminderTime: String = "",
    val repeatInterval: String = "",
    val isDone: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val taskRepository: TaskRepository,
    private val durationFormatter: DurationFormatter,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState

    private val taskId: Int = savedStateHandle.get<Int>("taskId") ?: 0

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            try {
                val task = taskRepository.getTaskById(taskId)
                task?.let {
                    val duration = calculateTotalDuration(task.startTime, task.endTime)

                    _uiState.value = TaskUiState(
                        title = task.title,
                        description = task.description,
                        startTime = formatTime(task.startTime),
                        endTime = formatTime(task.endTime),
                        totalDuration = durationFormatter.format(duration),
                        isLoading = false,
                        errorMessage = null,
                        date = formatDate(task.date),
                        reminderTime = task.reminderTime.toString(),
                        repeatInterval = task.repeatInterval.toString(),
                        isDone = task.isDone
                    )
                }
            } catch (e: Exception) {
                _uiState.value = TaskUiState(
                    isLoading = false,
                    errorMessage = "Ошибка загрузки задачи"
                )
            }
        }
    }

    private fun calculateTotalDuration(
        startMillis: Long,
        endMillis: Long,
    ): Duration =
        Duration.between(
            Instant.ofEpochMilli(startMillis).atZone(ZoneId.systemDefault()).toLocalTime(),
            Instant.ofEpochMilli(endMillis).atZone(ZoneId.systemDefault()).toLocalTime()
        ).let { if (it.isNegative) it.plusDays(1) else it }
}
