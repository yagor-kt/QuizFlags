package com.example.quizflags.ui.settings

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.quizflags.QuizApplication
import com.example.quizflags.R
import com.example.quizflags.navigation.Routes
import com.example.quizflags.ui.theme.QuizFlagsTheme

// Экран настроек: навигация по разделам и служебные действия.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    vm: SettingsViewModel,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current
    val app = context.applicationContext as QuizApplication
    val isLoggedIn by app.userPreferences.isLoggedInFlow.collectAsStateWithLifecycle(initialValue = false)

    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
        ) {
            item {
                SettingsItem(stringResource(R.string.view_flags)) {
                    onNavigate(Routes.Flags.route)
                }
            }
            item {
                SettingsItem(stringResource(R.string.login_account)) {
                    onNavigate(Routes.Login.route)
                }
            }
            item {
                SettingsItem(stringResource(R.string.stats)) {
                    onNavigate(Routes.Stats.route)
                }
            }
            item {
                SettingsItem(stringResource(R.string.leaders)) {
                    onNavigate(Routes.Leaders.route)
                }
            }
            item {
                SettingsItem(stringResource(R.string.reset_statistics)) {
                    showResetDialog = true
                }
            }
            item {
                SettingsItem(stringResource(R.string.about)) {
                    onNavigate(Routes.About.route)
                }
            }
            item {
                SettingsItem(stringResource(R.string.logout)) {
                    if (isLoggedIn) {
                        vm.logout()
                        onLogout
                    } else {
                        (context as? Activity)?.finish()
                    }
                }
            }
        }
    }

    // Диалог подтверждения сброса статистики.
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_dialog_title)) },
            text = { Text(stringResource(R.string.reset_dialog_message)) },
            confirmButton = {
                Button(onClick = {
                    vm.clearStatistics()
                    showResetDialog = false
                }) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

@Composable
private fun SettingsItem(
    text: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    // Предпросмотр только статичной части списка пунктов.
    QuizFlagsTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Настройки") })
            },
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                item { SettingsItem("Просмотр всех флагов", {}) }
                item { SettingsItem("Вход в аккаунт", {}) }
                item { SettingsItem("Статистика", {}) }
                item { SettingsItem("Таблица лидеров", {}) }
                item { SettingsItem("Сброс статистики", {}) }
                item { SettingsItem("Об игре", {}) }
                item { SettingsItem("Выход", {}) }
            }
        }
    }
}