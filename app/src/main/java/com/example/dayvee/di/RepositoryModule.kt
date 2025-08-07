package com.example.dayvee.di

import com.example.dayvee.data.repository.TaskRepositoryImpl
import com.example.dayvee.data.repository.UserRepositoryImpl
import com.example.dayvee.domain.repository.TaskRepository
import com.example.dayvee.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository

}
