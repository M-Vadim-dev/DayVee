package com.example.dayvee.ui.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.domain.model.TaskPriority
import com.example.dayvee.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

data class TaskStats(
    val total: Int = 0,
    val completedCount: Int = 0,
    val completedPercent: Float = 0f,
    val inProgressCount: Int = 0,
    val inProgressPercent: Float = 0f,
    val pendingCount: Int = 0,
    val pendingPercent: Float = 0f,
    val byPriority: Map<TaskPriority, Int> = emptyMap()
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskStats())
    val uiState: StateFlow<TaskStats> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                taskRepository.getTotalTasks(),
                taskRepository.getCompletedTasks(),
                taskRepository.getInProgressTasks(),
                taskRepository.getPendingTasks(),
                taskRepository.getTasksCountByPriority()
            ) { total, completed, inProgress, pending, byPriorityList ->

                val totalCount = total.toFloat()
                val completedRaw = if (totalCount > 0f) completed.toFloat() / totalCount else 0f
                val inProgressRaw = if (totalCount > 0f) inProgress.toFloat() / totalCount else 0f
                val completedPercent = (completedRaw * 100f).roundToInt() / 100f
                val inProgressPercent = (inProgressRaw * 100f).roundToInt() / 100f
                val pendingCount = total - completed - inProgress
                val pendingPercent = if (totalCount > 0f) 1f - completedPercent - inProgressPercent else 0f

                TaskStats(
                    total = total,
                    completedCount = completed,
                    inProgressCount = inProgress,
                    pendingCount = pendingCount,
                    completedPercent = completedPercent,
                    inProgressPercent = inProgressPercent,
                    pendingPercent = pendingPercent,
                    byPriority = byPriorityList.associate { it.priority to it.count }
                )

            }.collect { stats ->
                _uiState.value = stats
            }
        }
    }
}
