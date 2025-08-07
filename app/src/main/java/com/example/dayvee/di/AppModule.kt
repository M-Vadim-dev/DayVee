package com.example.dayvee.di

import android.content.Context
import androidx.work.WorkManager
import com.example.dayvee.data.local.dao.TaskDao
import com.example.dayvee.data.local.dao.UserDao
import com.example.dayvee.data.local.db.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

    @IoDispatcher
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDataBase =
        AppDataBase.getInstance(context)

    @Provides
    fun provideTaskDao(appDatabase: AppDataBase): TaskDao = appDatabase.taskDao()

    @Provides
    fun provideUserDao(appDatabase: AppDataBase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

//    @Provides
//    @Singleton
//    fun provideOkHttpClient(): OkHttpClient {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//        return OkHttpClient.Builder().addInterceptor(logging).build()
//    }

//    @Provides
//    @Singleton
//    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(API_BASE_URL)
//            .client(okHttpClient)
//            .addConverterFactory(Json.asConverterFactory(CONTENT_TYPE.toMediaType()))
//            .build()
//    }
}
