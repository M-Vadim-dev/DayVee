package com.example.dayvee.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.dayvee.data.local.converter.TaskIconConverter
import com.example.dayvee.domain.model.TaskIcon
import com.example.dayvee.domain.model.TaskPriority
import java.time.LocalDate

@Entity(tableName = "task")
@TypeConverters(TaskIconConverter::class)
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
    @ColumnInfo(name = "is_started") val isStarted: Boolean = false,
    val priority: TaskPriority = TaskPriority.NONE,
    val icon: TaskIcon = TaskIcon.Default,
)