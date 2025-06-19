package com.example.dayvee.data

import com.example.dayvee.data.local.dao.UserDao
import com.example.dayvee.data.mapper.toDomain
import com.example.dayvee.data.mapper.toEntity
import com.example.dayvee.di.AppModule.IoDispatcher
import com.example.dayvee.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getActiveUser(): User? = withContext(ioDispatcher) {
        userDao.getActiveUser()?.toDomain()
    }

    suspend fun initUserIfNotExists(defaultUser: User): User =
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