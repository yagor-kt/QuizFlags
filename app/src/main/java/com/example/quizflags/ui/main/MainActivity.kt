package com.example.quizflags.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.quizflags.navigation.NavGraph
import com.example.quizflags.ui.theme.QuizFlagsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizFlagsTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}