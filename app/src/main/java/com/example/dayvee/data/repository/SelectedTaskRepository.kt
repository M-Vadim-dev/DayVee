package com.example.dayvee.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedTaskRepository @Inject constructor() {
    private val _selectedTaskId = MutableStateFlow<Int?>(null)
    val selectedTaskId: StateFlow<Int?> = _selectedTaskId

    fun selectTask(taskId: Int?) {
        _selectedTaskId.value = taskId
    }

    fun clearSelectedTaskId() {
        _selectedTaskId.value = null
    }
}