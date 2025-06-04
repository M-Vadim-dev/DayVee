package com.example.dayvee.data.local.entity

import androidx.room.PrimaryKey

data class UserEntity(
    @PrimaryKey
    val id: Int,
    val status: String,
    val username: String,
    val password: String,
    val email: String,
    val question: String,
    val answer: String,
)
