package com.example.dayvee.data.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
    val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    val NOTIFICATIONS = booleanPreferencesKey("notifications")
    val LANGUAGE = stringPreferencesKey("language")
}