package com.example.quizflags.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizflags.QuizApplication
import com.example.quizflags.data.repository.StatsRepository
import com.example.quizflags.data.prefs.UserPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val statsRepository: StatsRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    /** Очищает всю статистику. */
    fun clearStatistics() {
        viewModelScope.launch {
            statsRepository.clearAll()
        }
    }

    /** Выход из аккаунта. */
    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication
                SettingsViewModel(
                    statsRepository = app.statsRepository,
                    userPreferences = UserPreferences.getInstance(app)
                )
            }
        }
    }
}