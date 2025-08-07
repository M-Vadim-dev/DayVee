package com.example.dayvee.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dayvee.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE date = :date AND user_id = :userId ORDER BY start_time ASC")
    fun getTasksForDate(userId: Int, date: LocalDate): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE date = :date AND user_id = :userId ORDER BY start_time ASC")
    suspend fun getTasksForDateOnce(userId: Int, date: LocalDate): List<TaskEntity>

    @Query("SELECT * FROM task WHERE date BETWEEN :start AND :end ORDER BY date ASC, start_time ASC")
    fun getTasksForDateRange(start: LocalDate, end: LocalDate): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("SELECT * FROM task ORDER BY date ASC, start_time ASC")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Query("UPDATE task SET is_done = :isDone WHERE id = :id")
    suspend fun toggleDone(id: Int, isDone: Boolean)
}