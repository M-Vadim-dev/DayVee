package com.example.dayvee.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val darkMode: Flow<Boolean>
    val isFirstLaunch: Flow<Boolean>
    val notifications: Flow<Boolean>
    val language: Flow<String>
    val useManualTimeInputs: Flow<Boolean>

    suspend fun setDarkMode(enabled: Boolean)
    suspend fun setNotifications(enabled: Boolean)
    suspend fun setLanguage(lang: String)
    suspend fun setLaunched()
    suspend fun setUseManualTimeInputs(enabled: Boolean)
}
