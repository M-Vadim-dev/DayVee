package com.example.dayvee.ui.screens.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

sealed class SettingItem {
    data class GroupTitle(
        @field:StringRes val titleRes: Int,
    ) : SettingItem()

    data class Switch(
        @field:DrawableRes val iconRes: Int,
        @field:StringRes val titleRes: Int,
        val checked: Boolean,
        val onToggle: (Boolean) -> Unit,
    ) : SettingItem()

    data class Navigation(
        @field:DrawableRes val iconRes: Int,
        @field:StringRes val titleRes: Int,
        @field:StringRes val valueRes: Int? = null,
        val onClick: () -> Unit,
    ) : SettingItem()

    data class Action(
        @field:DrawableRes val iconRes: Int,
        @field:StringRes val titleRes: Int,
        val onClick: () -> Unit,
    ) : SettingItem()
}

sealed class SettingsUiItem {
    data class GroupTitle(val id: String) : SettingsUiItem()
    data class Switch(
        val id: String,
        val checked: Boolean,
        val onToggle: (Boolean) -> Unit
    ) : SettingsUiItem()
    data class Navigation(
        val id: String,
        val onClick: () -> Unit
    ) : SettingsUiItem()
    data class Action(
        val id: String,
        val onClick: () -> Unit
    ) : SettingsUiItem()
}
