package com.example.dayvee.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    val description: String = "",
    val date: LocalDate,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
)