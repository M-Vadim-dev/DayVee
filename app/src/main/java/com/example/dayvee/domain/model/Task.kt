package com.example.dayvee.domain.model

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class Task(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val date: LocalDate,
    val isDone: Boolean = false
)