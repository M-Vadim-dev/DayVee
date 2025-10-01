package com.example.dayvee.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.data.repository.SelectedTaskRepository
import com.example.dayvee.data.repository.SharedDateRepository
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.User
import com.example.dayvee.domain.repository.TaskRepository
import com.example.dayvee.domain.repository.UserRepository
import com.example.dayvee.domain.usecase.GreetingUseCase
import com.example.dayvee.managers.TaskStartAlarmManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val sharedDateRepository: SharedDateRepository,
    private val selectedTaskRepository: SelectedTaskRepository,
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val greetingUseCase: GreetingUseCase,
    private val taskStartAlarmManager: TaskStartAlarmManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())

    init {
        initUserAndTasks()
        observeProgress()
    }

    internal fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
            taskStartAlarmManager.cancelTaskStartAlarm(task.id.hashCode().toLong(), task.title)
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

    internal fun onTaskSelected(taskId: Int) {
        selectedTaskRepository.selectTask(taskId)
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

    internal fun markTaskDone(taskId: Int) {
        viewModelScope.launch {
            val currentTasks = _tasks.value
            val taskToMark = currentTasks.find { it.id == taskId }
            if (taskToMark != null && !taskToMark.isDone) {
                val updatedTask = taskToMark.copy(isDone = true)
                taskRepository.updateTask(updatedTask)

                val updatedTasks = currentTasks.map {
                    if (it.id == taskId) updatedTask else it
                }
                _tasks.value = updatedTasks
            }
        }
    }

    private fun initUserAndTasks() {
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
                taskRepository.getTasksForDate(activeUser.uid, selectedDate)
                    .collect { tasks ->
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
    }

    private fun observeProgress() {
        viewModelScope.launch {
            tickerFlow().collect {
                val now = System.currentTimeMillis()
                val oldTasks = _tasks.value

                val updatedTasks = oldTasks.map { task -> updateTaskStatus(task, now) }

                val progressMap = updatedTasks.associate { task ->
                    val progress = if (task.isDone) 1f
                    else {
                        val total = (task.endTime - task.startTime).coerceAtLeast(1L)
                        val elapsed = (now - task.startTime).coerceIn(0L, total)
                        elapsed.toFloat() / total
                    }
                    val remaining = (task.endTime - now).coerceAtLeast(0L)
                    task.id to TaskProgressInfo(task.id, progress, remaining)
                }

                _uiState.update {
                    it.copy(tasks = updatedTasks, tasksProgress = progressMap)
                }

                updatedTasks.forEachIndexed { index, task ->
                    val oldTask = oldTasks[index]
                    if (task.isStarted != oldTask.isStarted || task.isDone != oldTask.isDone) {
                        viewModelScope.launch {
                            taskRepository.updateTask(task)
                        }
                    }
                }

                _tasks.value = updatedTasks
            }
        }
    }

    private fun tickerFlow() = flow {
        while (true) {
            emit(Unit)
            delay(1000L)
        }
    }

    private fun updateGreeting() {
        val greeting = greetingUseCase.getGreeting()
        _uiState.update { it.copy(greeting = greeting) }
    }

    private fun updateTaskStatus(task: Task, now: Long = System.currentTimeMillis()): Task {
        val startMillis = task.startTime
        val endMillis = task.endTime

        val isDone = now >= endMillis
        val isStarted = now >= startMillis && now < endMillis && !isDone

        return task.copy(isDone = isDone, isStarted = isStarted)
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
