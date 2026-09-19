package com.example.quizflags

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Preview
import com.example.quizflags.ui.theme.QuizFlagsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Внутри setContent в MainActivity:
            QuizFlagsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Передаем отступы, чтобы Scaffold не перекрывал текст
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)

@Composable
fun GreetingPreview() {
    QuizFlagsTheme {
        // Добавляем отступ от шторки специально для превью
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Greeting("Android")
        }
    }
}