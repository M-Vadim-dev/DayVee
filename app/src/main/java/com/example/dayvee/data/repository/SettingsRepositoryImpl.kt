package com.example.dayvee.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.dayvee.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override val darkMode: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[DARK_MODE] ?: true
    }

    override val notifications: Flow<Boolean> =
        dataStore.data.map { it[NOTIFICATIONS] ?: true }

    override val language = dataStore.data.map { it[LANGUAGE] ?: "ru" }

    override val isFirstLaunch: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[IS_FIRST_LAUNCH] ?: true }

    override val useManualTimeInputs: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[USE_MANUAL_TIME_INPUTS] ?: false }

    override suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[DARK_MODE] = enabled }
    }

    override suspend fun setNotifications(enabled: Boolean) {
        dataStore.edit { it[NOTIFICATIONS] = enabled }
    }

    override suspend fun setLanguage(lang: String) {
        dataStore.edit { it[LANGUAGE] = lang }
    }

    override suspend fun setLaunched() {
        dataStore.edit { it[IS_FIRST_LAUNCH] = false }
    }

    override suspend fun setUseManualTimeInputs(enabled: Boolean) {
        dataStore.edit { it[USE_MANUAL_TIME_INPUTS] = enabled }
    }

    companion object {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val LANGUAGE = stringPreferencesKey("language")
        val USE_MANUAL_TIME_INPUTS = booleanPreferencesKey("use_manual_time_inputs")
    }
}
