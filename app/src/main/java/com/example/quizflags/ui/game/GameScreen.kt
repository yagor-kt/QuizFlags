package com.example.quizflags.ui.game

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quizflags.R
import com.example.quizflags.data.local.entity.FlagEntity
import com.example.quizflags.navigation.Routes
import com.example.quizflags.ui.theme.QuizFlagsTheme

// Экран игрового процесса: таймер, жизни, текущий флаг и варианты ответа.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    vm: GameViewModel,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    GameContent(
        uiState = uiState,
        onAnswer = vm::onAnswer,
        onBack = vm::onBackPressed,
        onConfirmExit = vm::confirmExit,
        onDismissExit = vm::dismissExitDialog,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameContent(
    uiState: GameUiState,
    onAnswer: (String) -> Unit,
    onBack: () -> Unit,
    onConfirmExit: () -> Unit,
    onDismissExit: () -> Unit,
) {
    val minutes = uiState.timeLeft / 60
    val seconds = uiState.timeLeft % 60

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.playerName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                actions = {
                    Text(
                        text = stringResource(R.string.game_timer, minutes, seconds),
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stringResource(R.string.game_score, uiState.score),
                        modifier = Modifier.padding(end = 16.dp),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Spacer(Modifier.height(44.dp))
            // Индикатор жизней: три сердечка.
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(3) { i ->
                    Icon(
                        imageVector = if (i < uiState.lives) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Текущий флаг из assets.
            val flag = uiState.currentFlag
            val flagPainter = rememberVectorPainter(Icons.Default.LocationOn)

            AsyncImage(
                model = "file:///android_asset/flags/${flag?.imageName.orEmpty()}.png",
                contentDescription = flag?.name.orEmpty(),
                fallback = flagPainter,
                error = flagPainter,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1.5f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )

//            Spacer(Modifier.height(24.dp))
            Spacer(modifier = Modifier.weight(1f))

            // Варианты ответов в сетке 2 × 2.
            val options = uiState.options

            Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
                OptionButton(options.getOrNull(0), onAnswer, Modifier.weight(1f).fillMaxHeight())
                OptionButton(options.getOrNull(1), onAnswer, Modifier.weight(1f).fillMaxHeight())
            }
            Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
                OptionButton(options.getOrNull(2), onAnswer, Modifier.weight(1f).fillMaxHeight())
                OptionButton(options.getOrNull(3), onAnswer, Modifier.weight(1f).fillMaxHeight())
            }

        }
    }

    // Диалог подтверждения выхода.
    if (uiState.exitDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissExit,
            title = { Text(stringResource(R.string.exit_dialog_title)) },
            confirmButton = {
                Button(onClick = onConfirmExit) {
                    Text(stringResource(R.string.exit))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissExit) {
                    Text(stringResource(R.string.cancel))
                }
            },
        )
    }
}

/*@Composable
private fun OptionButton(
    option: String?,
    onAnswer: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (option == null) return
    OutlinedButton(
        onClick = { onAnswer(option) },
        modifier = modifier
            .padding(6.dp)
            .fillMaxWidth(),
    ) {
        Text(option)
    }
}*/

@Composable
private fun OptionButton(
    option: String?,
    onAnswer: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    if (option == null) {
        Spacer(modifier = modifier)
        return
    }

    OutlinedButton (
        onClick = { onAnswer(option) },
        modifier = modifier, // Сюда из Row прилетают .weight(1f).fillMaxHeight()
        shape = RectangleShape, // Делает углы острыми, чтобы кнопки сливались стык в стык
        contentPadding = PaddingValues(0.dp), // Убираем внутренние ограничения для текста
        elevation = null // Отключаем тени, чтобы кнопки были плоскими и не перекрывали друг друга
    ) {
        Text(option, fontSize = 20.sp)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    QuizFlagsTheme {
        GameContent(
            uiState = GameUiState(
                currentFlag = FlagEntity(id = 1, name = "Россия", imageName = "ru"),
                options = listOf("Россия", "США", "Китай", "Япония"),
                score = 5,
                lives = 2,
                timeLeft = 42,
                playerName = "Игрок",
                isGameOver = false,
                exitDialogVisible = false,
            ),
            onAnswer = {},
            onBack = {},
            onConfirmExit = {},
            onDismissExit = {},
        )
    }
}