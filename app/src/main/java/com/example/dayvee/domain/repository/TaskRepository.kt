package com.example.dayvee.domain.repository

import com.example.dayvee.domain.model.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getTasksForDate(userId: Int, date: LocalDate): Flow<List<Task>>

    suspend fun getTasksForDateOnce(userId: Int, date: LocalDate): List<Task>

    suspend fun addTask(task: Task): Long

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)

}