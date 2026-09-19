package com.example.quizflags.ui.result

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/**
 * Просто передаёт score и reason из SavedStateHandle в UI.
 */
class ResultViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    val score: Int = savedStateHandle["score"] ?: 0
    val reason: String = savedStateHandle["reason"] ?: ""

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ResultViewModel(createSavedStateHandle())
            }
        }
    }
}