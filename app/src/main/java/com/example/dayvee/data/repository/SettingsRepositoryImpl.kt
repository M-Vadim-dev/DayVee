package com.example.dayvee.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.dayvee.data.dataStore.SettingsKeys
import com.example.dayvee.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {
    override val darkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[SettingsKeys.DARK_MODE] ?: false
    }

    override val notifications: Flow<Boolean> =
        dataStore.data.map { it[SettingsKeys.NOTIFICATIONS] ?: true }

    override val language = dataStore.data.map { it[SettingsKeys.LANGUAGE] ?: "ru" }

    override val isFirstLaunch: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[SettingsKeys.IS_FIRST_LAUNCH] ?: true }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[SettingsKeys.DARK_MODE] = enabled }
    }

    override suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { it[SettingsKeys.NOTIFICATIONS] = enabled }
    }

    override suspend fun setLanguage(lang: String) {
        dataStore.edit { it[SettingsKeys.LANGUAGE] = lang }
    }

    override suspend fun setLaunched() {
        dataStore.edit { it[SettingsKeys.IS_FIRST_LAUNCH] = false }
    }
}
