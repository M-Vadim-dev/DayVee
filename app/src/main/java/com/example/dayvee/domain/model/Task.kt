package com.example.dayvee.domain.model

import java.time.LocalDate

data class Task(
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val description: String = "",
    val date: LocalDate,
    val startTime: Long,
    val endTime: Long,
    val reminderTime: Long? = null,
    val repeatInterval: String? = null,
    val isDone: Boolean = false,
    val isStarted: Boolean = false,
    val priority: TaskPriority = TaskPriority.NONE,
    val icon: TaskIcon = TaskIcon.Default,
)