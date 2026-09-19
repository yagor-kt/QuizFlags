package com.example.quizflags.ui.leaders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizflags.QuizApplication
import com.example.quizflags.data.local.LeaderRow
import com.example.quizflags.data.repository.StatsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class LeadersViewModel(statsRepository: StatsRepository) : ViewModel() {

    val leadersFlow: StateFlow<List<LeaderRow>> = statsRepository.getLeaders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication
                LeadersViewModel(app.statsRepository)
            }
        }
    }
}