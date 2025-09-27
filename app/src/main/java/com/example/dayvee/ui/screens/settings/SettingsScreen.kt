package com.example.dayvee.ui.screens.settings

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dayvee.R
import com.example.dayvee.ui.theme.DayVeeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val darkMode by viewModel.darkMode.collectAsState()
    val notifications by viewModel.notifications.collectAsState(initial = true)
    val language by viewModel.language.collectAsState(initial = "ru")

    val state = viewModel.state.collectAsState().value

    SettingsScreenContent(
        darkMode = darkMode,
        notifications = notifications,
        currentLanguage = language,
        onBackClick = onBackClick,
        onToggleDarkMode = { viewModel.toggleDarkMode(it) },
        onToggleNotifications = { viewModel.toggleNotifications(it) },
        onLanguageSelected = { viewModel.setLanguage(it) }
    )

}

@Composable
private fun SettingsScreenContent(
    darkMode: Boolean,
    notifications: Boolean,
    currentLanguage: String,
    onBackClick: () -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SettingsTopAppBar(onBackClick = onBackClick)
        },
        containerColor = Color.Transparent
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.settings_dark_mode))
                    Switch(
                        checked = darkMode,
                        onCheckedChange = onToggleDarkMode
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(R.string.settings_notifications))
                    Switch(
                        checked = notifications,
                        onCheckedChange = onToggleNotifications
                    )
                }
            }

            item {
                LanguageRow(
                    currentLang = currentLanguage,
                    onLanguageSelected = onLanguageSelected
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    currentLang: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf("English" to "en", "Русский" to "ru", "Français" to "fr")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(R.string.settings_language))
        Text(text = languages.first { it.second == currentLang }.first)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (label, code) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onLanguageSelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTopAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.nav_setting),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.text_description_task)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

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
            onLanguageSelected = {},
        )
    }
}
