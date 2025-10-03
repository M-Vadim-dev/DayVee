package com.example.dayvee.ui.screens.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.dayvee.R
import com.example.dayvee.ui.components.CustomHorizontalDivider
import com.example.dayvee.ui.theme.CriticalRed
import com.example.dayvee.ui.theme.DayVeeTheme
import com.example.dayvee.ui.theme.SandyBrown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val darkMode by viewModel.darkMode.collectAsState()
    val notifications by viewModel.notifications.collectAsState(initial = true)
    val language by viewModel.language.collectAsState(initial = "ru")
    val manualTimeInputs by viewModel.manualTimeInputs.collectAsState()

    SettingsScreenContent(
        darkMode = darkMode,
        notifications = notifications,
        manualTimeInputs = manualTimeInputs,
        currentLanguage = language,
        onBackClick = onBack,
        onToggleDarkMode = { viewModel.toggleDarkMode(it) },
        onToggleNotifications = { viewModel.toggleNotifications(it) },
        onToggleUseManualTimeInputs = { viewModel.toggleUseManualTimeInputs(it) },
        onLanguageSelected = { viewModel.setLanguage(it) }
    )

}

@Composable
private fun SettingsScreenContent(
    darkMode: Boolean,
    notifications: Boolean,
    manualTimeInputs: Boolean,
    currentLanguage: String,
    onBackClick: () -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleUseManualTimeInputs: (Boolean) -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    Scaffold(
        topBar = { SettingsTopAppBar(onBackClick = onBackClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                SettingsNavigationRow(
                    label = "Пользовательский аккаунт",
                    icon = painterResource(R.drawable.ic_account_circle),
                    description = "Управление профилем и данными",
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { }
                )
                CustomHorizontalDivider()
                SettingsNavigationRow(
                    label = "Обновить до Premium",
                    icon = painterResource(R.drawable.ic_premium),
                    description = "Откройте дополнительные функции",
                    iconColor = SandyBrown,
                    onClick = { }
                )
            }
            Text(
                text = "Общие",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Column {
                    SettingsRow(
                        label = stringResource(R.string.settings_dark_mode),
                        icon = painterResource(R.drawable.ic_clipboard_brush),
                        description = "Использовать тёмное оформление",
                        checked = darkMode,
                        onCheckedChange = onToggleDarkMode,
                    )
                    CustomHorizontalDivider()
                    SettingsRow(
                        label = stringResource(R.string.settings_notifications),
                        icon = painterResource(R.drawable.ic_notifications),
                        description = "Уведомления о задачах",
                        checked = notifications,
                        onCheckedChange = onToggleNotifications
                    )
                    CustomHorizontalDivider()
                    LanguageRow(
                        currentLang = currentLanguage,
                        onLanguageSelected = onLanguageSelected
                    )
                    CustomHorizontalDivider()
                    SettingsRow(
                        label = "Первый день недели",
                        icon = painterResource(R.drawable.ic_assignment),
                        description = "Задайте начало календаря",
                        onCheckedChange = {}
                    )
                    CustomHorizontalDivider()
                    SettingsRow(
                        label = "Формат времени",
                        icon = painterResource(R.drawable.ic_schedule),
                        description = "12- или 24-часовой формат отображения",
                        onCheckedChange = {}
                    )
                    CustomHorizontalDivider()
                    SettingsRow(
                        label = "Звук завершеня задачи",
                        icon = painterResource(R.drawable.ic_sound),
                        description = "Включить звуковое уведомление при завершении",
                        onCheckedChange = {}
                    )
                    CustomHorizontalDivider()
                    SettingsRow(
                        label = "Ручной ввод времени",
                        icon = painterResource(R.drawable.ic_keyboard),
                        description = "Вводить время вручную с клавиатуры",
                        checked = manualTimeInputs,
                        onCheckedChange = onToggleUseManualTimeInputs
                    )
                    CustomHorizontalDivider()
                    SettingsNavigationRow(
                        label = "Виджеты",
                        icon = painterResource(R.drawable.ic_dataset_fill),
                        description = "Настроить отображение виджетов на главном экране",
                        onClick = {}
                    )
                }
            }
            Text(
                text = "Поддержка",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                SettingsNavigationRow(
                    label = "Помощь",
                    icon = painterResource(R.drawable.ic_help_circle),
                    description = "Ответы на частые вопросы",
                    onClick = { }
                )
                CustomHorizontalDivider()
                SettingsNavigationRow(
                    label = "Обратная связь",
                    icon = painterResource(R.drawable.ic_message),
                    description = "Включите настройку отображения ручного ввода с клавиатуры",
                    onClick = { }
                )
                CustomHorizontalDivider()
                SettingsNavigationRow(
                    label = "Оценить приложение",
                    icon = painterResource(R.drawable.ic_like_outline),
                    description = "Поставьте оценку",
                    iconColor = CriticalRed,
                    onClick = { }
                )
                CustomHorizontalDivider()
                SettingsNavigationRow(
                    label = "Поделиться приложением",
                    icon = painterResource(R.drawable.ic_share),
                    description = "Отправить ссылку друзьям",
                    onClick = { }
                )
                CustomHorizontalDivider()
                SettingsNavigationRow(
                    label = "О приложении",
                    icon = painterResource(R.drawable.ic_info_circle),
                    description = "Версия, лицензия и информация о разработчике",
                    iconColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    icon: Painter,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    description: String,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedThumbColor = MaterialTheme.colorScheme.surfaceVariant,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                uncheckedBorderColor = Color.Transparent,
            )
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    label: String,
    icon: Painter,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    description: String = "",
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1
            )
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageRow(
    currentLang: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English" to "en", "Русский" to "ru")
    val currentLabel = languages.first { it.second == currentLang }.first

    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_language),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.settings_language),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.small)
        ) {
            TextField(
                value = currentLabel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.menuAnchor(PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            ) {
                languages.forEach { (label, code) ->
                    val isSelected = code == currentLang
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = label,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        onClick = {
                            onLanguageSelected(code)
                            expanded = false
                            focusManager.clearFocus()
                        },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                else Color.Transparent,
                            )
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.nav_setting),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {

            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_left),
                    contentDescription = stringResource(R.string.nav_back),
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.size(32.dp)
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContentPreview() {

    DayVeeTheme {
        SettingsScreenContent(
            onBackClick = {},
            onToggleDarkMode = {},
            darkMode = false,
            notifications = false,
            currentLanguage = "ru",
            onToggleNotifications = {},
            onToggleUseManualTimeInputs = {},
            manualTimeInputs = true,
            onLanguageSelected = {},
        )
    }
}
