package com.example.dayvee.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val status: Int,
    val username: String,
    val password: String,
    val email: String,
    val question: String,
    val answer: String,
)
