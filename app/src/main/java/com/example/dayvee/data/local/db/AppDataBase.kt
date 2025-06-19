package com.example.dayvee.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dayvee.data.local.dao.TaskDao
import com.example.dayvee.data.local.dao.UserDao
import com.example.dayvee.data.local.db.DataBaseVersion.DATA_BASE_VERSION
import com.example.dayvee.data.local.entity.TaskEntity
import com.example.dayvee.data.local.entity.UserEntity
import com.example.dayvee.utils.Constants.DB_NAME
import com.example.dayvee.utils.Converters

object DataBaseVersion {
    internal const val DATA_BASE_VERSION = 1
}

@Database(
    entities = [
        TaskEntity::class,
        UserEntity::class,
    ],
    version = DATA_BASE_VERSION,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
