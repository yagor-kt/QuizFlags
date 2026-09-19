package com.example.quizflags.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizflags.QuizApplication
import com.example.quizflags.data.prefs.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Состояние экрана логина.
 */
data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val isLoggedIn: Boolean = false
)

/**
 * События навигации из экрана логина.
 */
sealed class LoginEvent {
    object NavigateBack : LoginEvent()
}

class LoginViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<LoginEvent>()
    val events: SharedFlow<LoginEvent> = _events.asSharedFlow()

    fun onLoginChange(value: String) {
        _uiState.update { it.copy(login = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    /** Вход под именем: сохраняем логин, ставим флаг авторизации и возвращаемся. */
    fun login() {
        viewModelScope.launch {
            val name = _uiState.value.login.trim()
            if (name.isNotEmpty()) {
                userPreferences.setUserName(name)
                userPreferences.setLoggedIn(true)
                _uiState.update { it.copy(isLoggedIn = true) }
                _events.emit(LoginEvent.NavigateBack)
            }
        }
    }

    /** Вход как гость: гарантируем guest id и возвращаемся. */
    fun loginAsGuest() {
        viewModelScope.launch {
            userPreferences.ensureGuestId()
            _events.emit(LoginEvent.NavigateBack)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication
                LoginViewModel(
                    userPreferences = UserPreferences.getInstance(app)
                )
            }
        }
    }
}