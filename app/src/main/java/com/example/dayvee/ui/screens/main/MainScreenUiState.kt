package com.example.dayvee.ui.screens.main

import androidx.compose.runtime.Immutable
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.domain.model.User
import java.time.LocalDate
import java.time.YearMonth

@Immutable
data class MainScreenUiState(
    val activeUser: User? = null,
    val greeting: String = "",
    val today: LocalDate = LocalDate.now(),
    val tasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMonth: YearMonth = YearMonth.from(LocalDate.now()),
    val isDatePickerVisible: Boolean = false,
    val isToday: Boolean = false,
    val isCompleted: Boolean = false,
    val tasksProgress: Map<Int, TaskProgressInfo> = emptyMap(),
    val priority: TaskPriority = TaskPriority.NONE,
)

@Immutable
data class TaskProgressInfo(
    val taskId: Int,
    val progress: Float,
    val remainingMillis: Long,
)