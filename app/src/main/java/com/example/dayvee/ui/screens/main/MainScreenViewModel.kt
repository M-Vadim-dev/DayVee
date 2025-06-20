package com.example.dayvee.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.data.SharedDateRepository
import com.example.dayvee.data.TaskRepository
import com.example.dayvee.data.UserRepository
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.User
import com.example.dayvee.domain.usecase.GreetingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val sharedDateRepository: SharedDateRepository,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val greetingUseCase: GreetingUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    init {
        viewModelScope.launch {
            val defaultUser = User(
                status = 1,
                username = "default_user",
                password = "",
                email = "",
                question = "",
                answer = ""
            )
            userRepository.initUserIfNotExists(defaultUser)
            updateGreeting()

            sharedDateRepository.selectedDate.collectLatest { selectedDate ->
                val activeUser = userRepository.getActiveUser() ?: return@collectLatest
                val tasksFlow = taskRepository.getTasksForDate(activeUser.uid, selectedDate)
                tasksFlow.collect { tasks ->
                    val now = System.currentTimeMillis()
                    val updatedTasks = tasks.map { task ->
                        task.copy(isDone = now > task.endTime)
                    }

                    _tasks.value = updatedTasks

                    _uiState.update {
                        it.copy(
                            activeUser = activeUser,
                            selectedDate = selectedDate,
                            tasks = updatedTasks,
                            isToday = selectedDate == LocalDate.now()
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            _tasks.collectLatest { tasks ->
                if (tasks.isEmpty()) {
                    _uiState.update { it.copy(tasksProgress = emptyMap()) }
                    return@collectLatest
                }

                while (true) {
                    val nowMillis = System.currentTimeMillis()
                    val progressMap = tasks.associate { task ->
                        val taskProgressInfo = if (task.isDone || nowMillis >= task.endTime) {
                            TaskProgressInfo(task.id, 1f, 0L).also {
                                if (!task.isDone) {
                                    viewModelScope.launch {
                                        taskRepository.updateTask(task.copy(isDone = true))
                                    }
                                }
                            }
                        } else {
                            val startMillis = task.startTime
                            val endMillis = task.endTime
                            val total = (endMillis - startMillis).coerceAtLeast(1L)
                            val elapsed = (nowMillis - startMillis).coerceIn(0L, total)
                            val progress = elapsed.toFloat() / total
                            val remaining = (endMillis - nowMillis).coerceAtLeast(0L)

                            TaskProgressInfo(task.id, progress, remaining)
                        }

                        task.id to taskProgressInfo
                    }

                    _uiState.update { it.copy(tasksProgress = progressMap) }
                    delay(1000L)
                }
            }
        }
    }

    internal fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    internal fun setSelectedDate(date: LocalDate) {
        sharedDateRepository.updateDate(date)
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
        sharedDateRepository.updateDate(date)
        updateTasksForDate(date)
    }

    internal fun setCurrentMonth(month: YearMonth) {
        _uiState.update { currentState ->
            currentState.copy(currentMonth = month)
        }
    }

    internal fun setDatePickerVisibility(isVisible: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isDatePickerVisible = isVisible)
        }
    }

    private fun updateGreeting() {
        val greeting = greetingUseCase.getGreeting()
        _uiState.update { it.copy(greeting = greeting) }
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
