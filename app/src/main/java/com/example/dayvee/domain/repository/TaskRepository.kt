package com.example.dayvee.domain.repository

import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.model.TaskPriorityCount
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getTasksForDate(userId: Int, date: LocalDate): Flow<List<Task>>

    suspend fun getTasksForDateOnce(userId: Int, date: LocalDate): List<Task>

    suspend fun getTaskById(taskId: Int): Task?

    suspend fun addTask(task: Task): Long

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

    fun getTotalTasks(): Flow<Int>
    fun getCompletedTasks(): Flow<Int>
    fun getInProgressTasks(): Flow<Int>
    fun getPendingTasks(): Flow<Int>
    fun getTasksCountForDate(date: LocalDate): Flow<Int>
    fun getCompletedTasksForDate(date: LocalDate): Flow<Int>
    fun getPendingTasksForDate(date: LocalDate): Flow<Int>
    fun getTasksCountForRange(start: LocalDate, end: LocalDate): Flow<Int>
    fun getTasksCountByPriority(): Flow<List<TaskPriorityCount>>

}