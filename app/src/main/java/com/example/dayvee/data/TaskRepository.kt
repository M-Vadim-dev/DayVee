package com.example.dayvee.data

import com.example.dayvee.data.local.dao.TaskDao
import com.example.dayvee.di.AppModule.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun addTask(title: String, date: LocalDate) = withContext(ioDispatcher) {

    }

    suspend fun toggleTask(id: Int) = withContext(ioDispatcher) {

    }

    suspend fun deleteTask(id: Int) = withContext(ioDispatcher) {

    }
}
