package com.example.quizflags.ui.result

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

// Экран результата игры: итоговый счёт и причина завершения.
@Composable
fun ResultScreen(
    score: Int,
    reason: String,
    onRestart: () -> Unit,
    onBackToMain: () -> Unit,
) {
    ResultContent(
        score = score,
        reason = reason,
        onPlayAgain = onRestart,
        onMainMenu = onBackToMain,
    )
}

@Composable
private fun ResultContent(
    score: Int,
    reason: String,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit,
) {
    val reasonText = if (reason == "lives") {
        stringResource(R.string.reason_lives)
    } else {
        stringResource(R.string.reason_time)
    }

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
                text = stringResource(R.string.result_score, score),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = reasonText,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(48.dp))
            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.play_again))
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onMainMenu,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.main_menu))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultScreenPreview() {
    QuizFlagsTheme {
        ResultContent(
            score = 12,
            reason = "time",
            onPlayAgain = {},
            onMainMenu = {},
        )
    }
}