package com.example.quizflags.ui.flags

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizflags.QuizApplication
import com.example.quizflags.data.local.entity.FlagEntity
import com.example.quizflags.data.repository.FlagsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FlagsViewModel(flagsRepository: FlagsRepository) : ViewModel() {

    val flagsFlow: StateFlow<List<FlagEntity>> = flagsRepository.getAllFlags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication
                FlagsViewModel(app.flagsRepository)
            }
        }
    }
}