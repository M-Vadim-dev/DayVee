package com.example.dayvee.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dayvee.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE status != 1 ORDER BY uid ASC")
    suspend fun readAllUser(): List<UserEntity>

    @Query("SELECT * FROM user WHERE uid = :uid")
    suspend fun readUser(uid: Int): UserEntity?

    @Query("SELECT password FROM user WHERE status = 1")
    suspend fun readActiveUserPassword(): String

    @Query("SELECT * FROM user WHERE status = 1")
    suspend fun getActiveUser(): UserEntity?

    @Query("DELETE FROM user WHERE uid = :uidUser")
    suspend fun deleteUser(uidUser: Int): Int

    @Query("UPDATE user SET status = 0 WHERE uid != :uidTarget")
    suspend fun deactivateOtherUsers(uidTarget: Int): Int

    @Query("UPDATE user SET status = 1 WHERE uid = :uidTarget")
    suspend fun activateUser(uidTarget: Int)

    @Query("SELECT COUNT(*) FROM user")
    suspend fun getUserCount(): Int
}
