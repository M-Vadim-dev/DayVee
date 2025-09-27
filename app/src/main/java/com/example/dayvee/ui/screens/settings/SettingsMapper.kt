package com.example.dayvee.ui.screens.settings

import com.example.dayvee.R

fun SettingsUiItem.toSettingItem(): SettingItem {
    return when (this) {
        is SettingsUiItem.GroupTitle -> SettingItem.GroupTitle(titleRes = R.string.text_title_task)
        is SettingsUiItem.Switch -> SettingItem.Switch(
            iconRes = R.drawable.ic_drop_fill,
            titleRes = R.string.nav_stats,
            checked = this.checked,
            onToggle = this.onToggle
        )
        is SettingsUiItem.Navigation -> SettingItem.Navigation(
            iconRes = R.drawable.ic_arrow_right,
            titleRes = R.string.nav_setting,
            valueRes = null,
            onClick = this.onClick
        )
        is SettingsUiItem.Action -> SettingItem.Action(
            iconRes = R.drawable.ic_mic,
            titleRes = R.string.text_minute,
            onClick = this.onClick
        )
    }
}