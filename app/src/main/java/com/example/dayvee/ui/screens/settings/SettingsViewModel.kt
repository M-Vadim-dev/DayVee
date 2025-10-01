package com.example.dayvee.ui.screens.settings

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dayvee.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val darkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val items: List<SettingsUiItem> = emptyList(),
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state

    val darkMode = settingsRepository.darkMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val notifications = settingsRepository.notifications.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        true
    )
    val language = settingsRepository.language.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "ru"
    )
    val manualTimeInputs = settingsRepository.useManualTimeInputs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotifications(enabled)
        }
    }

    fun toggleUseManualTimeInputs(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setUseManualTimeInputs(enabled)
        }
    }

    fun setLanguage(lang: String) = viewModelScope.launch {
        settingsRepository.setLanguage(lang)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
        }
    }
}
