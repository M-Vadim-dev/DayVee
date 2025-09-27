package com.example.dayvee.domain.model

data class User(
    val uid: Int = 0,
    val status: Int,
    val username: String,
    val password: String,
    val email: String,
    val question: String,
    val answer: String,
)