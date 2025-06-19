package com.example.dayvee.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: Int,
    @ColumnInfo(name = "title") val title: String,
    val description: String = "",
    val date: LocalDate,
    @ColumnInfo(name = "start_time") val startTime: Long,
    @ColumnInfo(name = "end_time") val endTime: Long,
    @ColumnInfo(name = "reminder_time") val reminderTime: Long? = null,
    @ColumnInfo(name = "repeat_interval") val repeatInterval: String? = null,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
)