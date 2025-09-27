package com.example.dayvee.data.mapper

import com.example.dayvee.data.local.entity.TaskEntity
import com.example.dayvee.domain.model.Task

fun TaskEntity.toDomain(): Task = Task(
    id = id,
    userId = userId,
    title = title,
    description = description,
    date = date,
    startTime = startTime,
    endTime = endTime,
    reminderTime = reminderTime,
    repeatInterval = repeatInterval,
    isDone = isDone,
    priority = priority,
    icon = icon,
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    userId = userId,
    title = title,
    description = description,
    date = date,
    startTime = startTime,
    endTime = endTime,
    reminderTime = reminderTime,
    repeatInterval = repeatInterval,
    isDone = isDone,
    priority = priority,
    icon = icon,
)