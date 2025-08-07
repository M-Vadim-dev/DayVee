package com.example.dayvee.data.repository

import com.example.dayvee.data.local.dao.UserDao
import com.example.dayvee.data.mapper.toDomain
import com.example.dayvee.data.mapper.toEntity
import com.example.dayvee.di.AppModule.IoDispatcher
import com.example.dayvee.domain.model.User
import com.example.dayvee.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : UserRepository {

    override suspend fun getActiveUser(): User? = withContext(ioDispatcher) {
        userDao.getActiveUser()?.toDomain()
    }

    override suspend fun initUserIfNotExists(defaultUser: User): User =
        withContext(ioDispatcher) {
            val activeUserEntity = userDao.getActiveUser()
            if (activeUserEntity != null) {
                return@withContext activeUserEntity.toDomain()
            }

            val userCount = userDao.getUserCount()
            if (userCount == 0) {
                val newUserId = userDao.insertUser(defaultUser.toEntity()).toInt()
                userDao.activateUser(newUserId)
                return@withContext userDao.readUser(newUserId)!!.toDomain()
            } else {
                return@withContext userDao.readAllUser().first().toDomain()
            }
        }
}