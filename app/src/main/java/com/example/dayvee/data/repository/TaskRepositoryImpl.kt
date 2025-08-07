package com.example.dayvee.data.repository

import com.example.dayvee.data.local.dao.TaskDao
import com.example.dayvee.data.mapper.toDomain
import com.example.dayvee.data.mapper.toEntity
import com.example.dayvee.di.AppModule.IoDispatcher
import com.example.dayvee.domain.model.Task
import com.example.dayvee.domain.repository.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TaskRepository {
    override fun getTasksForDate(userId: Int, date: LocalDate): Flow<List<Task>> =
        taskDao.getTasksForDate(userId, date).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getTasksForDateOnce(userId: Int, date: LocalDate): List<Task> {
        return taskDao.getTasksForDateOnce(userId, date).map { it.toDomain() }
    }

    override suspend fun addTask(task: Task): Long = withContext(ioDispatcher) {
        taskDao.insert(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) = withContext(ioDispatcher) {
        taskDao.delete(task.toEntity())
    }

    override suspend fun updateTask(task: Task) = withContext(ioDispatcher) {
        taskDao.update(task.toEntity())
    }
}
