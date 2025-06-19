package com.example.dayvee.data

import com.example.dayvee.data.local.dao.TaskDao
import com.example.dayvee.data.mapper.toDomain
import com.example.dayvee.data.mapper.toEntity
import com.example.dayvee.di.AppModule.IoDispatcher
import com.example.dayvee.domain.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    fun getTasksForDate(userId: Int, date: LocalDate): Flow<List<Task>> =
        taskDao.getTasksForDate(userId, date).map { entities -> entities.map { it.toDomain() } }

    suspend fun addTask(task: Task) = withContext(ioDispatcher) {
        taskDao.insert(task.toEntity())
    }

    suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        taskDao.delete(task.toEntity())
    }

    suspend fun updateTask(task: Task) = withContext(ioDispatcher) {
        taskDao.update(task.toEntity())
    }
}
