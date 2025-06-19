package com.example.dayvee.data.mapper

import com.example.dayvee.data.local.entity.UserEntity
import com.example.dayvee.domain.model.User

fun UserEntity.toDomain(): User = User(
    uid = uid,
    status = status,
    username = username,
    password = password,
    email = email,
    question = question,
    answer = answer
)

fun User.toEntity(): UserEntity = UserEntity(
    uid = uid,
    status = status,
    username = username,
    password = password,
    email = email,
    question = question,
    answer = answer
)