package com.example.quizflags.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quizflags.R
import com.example.quizflags.navigation.Routes
import com.example.quizflags.ui.theme.QuizFlagsTheme

@Composable
fun MainScreen(
    onNavigate: (String) -> Unit
) {
    MainContent(
        onStartGame = { onNavigate(Routes.Game.route) },
        onSettings = { onNavigate(Routes.Settings.route) },
    )
}

@Composable
private fun MainContent(
    onStartGame: () -> Unit,
    onSettings: () -> Unit,
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.main_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.start_game))
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onSettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.settings))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    QuizFlagsTheme {
        MainContent(onStartGame = {}, onSettings = {})
    }
}