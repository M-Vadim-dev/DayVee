package com.example.dayvee.ui.screens.main

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.data.SharedDateRepository
import com.example.dayvee.data.TaskRepository
import com.example.dayvee.data.UserRepository
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.User
import com.example.dayvee.domain.usecase.GreetingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

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
)

data class TaskProgressInfo(
    val taskId: Int,
    val progress: Float,
    val remainingMillis: Long,
)

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val sharedDateRepository: SharedDateRepository,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val greetingUseCase: GreetingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private var progressJob: Job? = null

    init {
        viewModelScope.launch {
            val defaultUser = User(
                status = 1,
                username = "default",
                password = "",
                email = "",
                question = "",
                answer = ""
            )
            val activeUser = userRepository.initUserIfNotExists(defaultUser)
            updateGreeting()

            sharedDateRepository.selectedDate
                .collectLatest { selectedDate ->
                    taskRepository.getTasksForDate(activeUser.uid, selectedDate).collect { tasks ->

                        val now = System.currentTimeMillis()
                        val updatedTasks = tasks.map { task ->
                            task.copy(isDone = now > task.endTime)
                        }

                        _uiState.update { currentState ->
                            currentState.copy(
                                activeUser = activeUser,
                                selectedDate = selectedDate,
                                tasks = updatedTasks,
                                isToday = selectedDate == LocalDate.now(),
                            )
                        }

                        startProgressTracking(updatedTasks)
                    }
                }
        }
    }

    private fun startProgressTracking(tasks: List<Task>) {
        progressJob?.cancel()
        if (tasks.isEmpty()) {
            _uiState.update { it.copy(tasksProgress = emptyMap()) }
            return
        }

        progressJob = viewModelScope.launch {
            while (true) {
                val nowMillis = System.currentTimeMillis()

                val progressMap = tasks.associate { task ->
                    val progressInfo = if (task.isDone || nowMillis >= task.endTime) {
                        // Задача завершена — прогресс 100%
                        TaskProgressInfo(
                            taskId = task.id,
                            progress = 1f,
                            remainingMillis = 0L
                        )
                    } else {
                        val startMillis = task.startTime
                        val endMillis = task.endTime

                        val totalDuration = (endMillis - startMillis).coerceAtLeast(1L)
                        val elapsed = (nowMillis - startMillis).coerceIn(0L, totalDuration)

                        val progress = elapsed.toFloat() / totalDuration.toFloat()
                        val remainingMillis = (endMillis - nowMillis).coerceAtLeast(0L)

                        TaskProgressInfo(
                            taskId = task.id,
                            progress = progress,
                            remainingMillis = remainingMillis
                        )
                    }

                    task.id to progressInfo
                }

                _uiState.update { it.copy(tasksProgress = progressMap) }
                delay(1000L)
            }
        }
    }

    private fun updateGreeting() {
        val greeting = greetingUseCase.getGreeting()
        _uiState.value = _uiState.value.copy(greeting = greeting)
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun setSelectedDate(date: LocalDate) {
        sharedDateRepository.updateDate(date)
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
        sharedDateRepository.updateDate(date)
        updateTasksForDate(date)
    }

    fun setCurrentMonth(month: YearMonth) {
        _uiState.update { currentState ->
            currentState.copy(currentMonth = month)
        }
    }

    fun setDatePickerVisibility(isVisible: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDatePickerVisible = isVisible)
        }
    }

    private fun updateTasksForDate(date: LocalDate) {
        val user = _uiState.value.activeUser ?: return

        viewModelScope.launch {
            taskRepository.getTasksForDate(user.uid, date).collect { tasks ->
                _uiState.update { it.copy(tasks = tasks) }

            }
        }
    }
}
