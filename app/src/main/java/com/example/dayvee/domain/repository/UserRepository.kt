package com.example.dayvee.domain.repository

import com.example.dayvee.domain.model.User

interface UserRepository {
    suspend fun getActiveUser(): User?

    suspend fun initUserIfNotExists(defaultUser: User): User
}