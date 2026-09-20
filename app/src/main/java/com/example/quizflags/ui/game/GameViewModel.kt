package com.example.quizflags.ui.game

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.quizflags.QuizApplication
import com.example.quizflags.data.local.entity.FlagEntity
import com.example.quizflags.data.local.entity.StatEntity
import com.example.quizflags.data.repository.FlagsRepository
import com.example.quizflags.data.repository.StatsRepository
import com.example.quizflags.data.prefs.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Ключи для SavedStateHandle (переживают уничтожение процесса)
private const val KEY_SCORE = "score"
private const val KEY_LIVES = "lives"
private const val KEY_TIME = "time"

// Причины завершения игры
private const val REASON_LIVES = "lives"
private const val REASON_TIME = "time"

// Начальные значения
private const val START_LIVES = 3
private const val START_TIME = 60

/**
 * Состояние экрана игры.
 */
data class GameUiState(
    val currentFlag: FlagEntity? = null,
    val options: List<String> = emptyList(),
    val score: Int = 0,
    val lives: Int = START_LIVES,
    val timeLeft: Int = START_TIME,
    val playerName: String = "",
    val isGameOver: Boolean = false,
    val exitDialogVisible: Boolean = false
)

/**
 * События навигации из игрового экрана.
 */
sealed class GameEvent {
    data class NavigateToResult(val score: Int, val reason: String) : GameEvent()
    object NavigateBack : GameEvent()
}

class GameViewModel(
    private val flagsRepository: FlagsRepository,
    private val statsRepository: StatsRepository,
    private val userPreferences: UserPreferences,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<GameEvent>()
    val events: SharedFlow<GameEvent> = _events.asSharedFlow()

    // Полный список флагов (исходные данные)
    private val allFlags = mutableListOf<FlagEntity>()

    // Очередь «оставшихся» флагов — из неё выбираем без повторов в рамках одной игры
    private val remainingFlags = mutableListOf<FlagEntity>()

    init {
        // Восстановление состояния после уничтожения процесса
        val savedScore = savedStateHandle.get<Int>(KEY_SCORE) ?: 0
        val savedLives = savedStateHandle.get<Int>(KEY_LIVES) ?: START_LIVES
        val savedTime = savedStateHandle.get<Int>(KEY_TIME) ?: START_TIME

        _uiState.update {
            it.copy(score = savedScore, lives = savedLives, timeLeft = savedTime)
        }

        loadPlayerName()
        loadFlagsAndStart()
    }

    /** Определяем имя игрока: из DataStore, если пусто — «гость_XXX». */
    private fun loadPlayerName() {
        viewModelScope.launch {
            val storedName = userPreferences.userNameFlow.first()
            val playerName = if (storedName.isBlank()) {
                val guestId = userPreferences.ensureGuestId()
                guestId
            } else {
                storedName
            }
            _uiState.update { it.copy(playerName = playerName) }
        }
    }

    /** Загружаем флаги, перемешиваем и начинаем игру с первым вопросом. */
    private fun loadFlagsAndStart() {
        viewModelScope.launch {
            val flags = flagsRepository.getAllFlags().first()
            allFlags.clear()
            allFlags.addAll(flags)
            remainingFlags.clear()
            remainingFlags.addAll(allFlags.shuffled())
            nextFlag()
            startTimer()
        }
    }

    /** Таймер обратного отсчёта (1 тик = 1 секунда). */
    private fun startTimer() {
        viewModelScope.launch {
            while (isActive) {
                delay(1_000)
                val state = _uiState.value
                if (state.isGameOver) return@launch

                val newTime = state.timeLeft - 1
                if (newTime <= 0) {
                    _uiState.update { it.copy(timeLeft = 0) }
                    savedStateHandle[KEY_TIME] = 0
                    finishGame(REASON_TIME)
                    return@launch
                }
                _uiState.update { it.copy(timeLeft = newTime) }
                savedStateHandle[KEY_TIME] = newTime
            }
        }
    }

    /** Выбирает следующий флаг и генерирует 4 варианта ответа. */
    private fun nextFlag() {
        val state = _uiState.value
        if (state.isGameOver || state.lives <= 0 || state.timeLeft <= 0) return

        // Если флаги закончились — перемешиваем заново (без повторов внутри одной партии)
        if (remainingFlags.isEmpty()) {
            remainingFlags.addAll(allFlags.shuffled())
        }

        val flag = remainingFlags.removeAt(0)
        val options = buildOptions(flag)
        _uiState.update { it.copy(currentFlag = flag, options = options) }
    }

    /** 1 правильный + 3 случайных чужих, перемешанные. */
    private fun buildOptions(correct: FlagEntity): List<String> {
        val wrongOptions = allFlags
            .filter { it.id != correct.id }
            .shuffled()
            .take(3)
            .map { it.name }
        return (wrongOptions + correct.name).shuffled()
    }

    /** Обработка выбранного варианта ответа. */
    fun onAnswer(option: String) {
        val state = _uiState.value
        if (state.isGameOver) return
        val flag = state.currentFlag ?: return

        if (option == flag.name) {
            // Правильный ответ: +1 очко
            val newScore = state.score + 1
            _uiState.update { it.copy(score = newScore) }
            savedStateHandle[KEY_SCORE] = newScore
            nextFlag()
        } else {
            // Неправильный ответ: −1 жизнь
            val newLives = state.lives - 1
            _uiState.update { it.copy(lives = newLives) }
            savedStateHandle[KEY_LIVES] = newLives

            if (newLives <= 0) {
                finishGame(REASON_LIVES)
            } else {
                nextFlag()
            }
        }
    }

    /** Завершение игры: сохраняем результат и отправляем событие навигации. */
    private fun finishGame(reason: String) {
        val state = _uiState.value
        if (state.isGameOver) return // защита от двойного завершения

        _uiState.update { it.copy(isGameOver = true) }

        val finalScore = state.score
        val playerName = state.playerName

        viewModelScope.launch {
            statsRepository.insertStat(
                StatEntity(
                    userName = playerName,
                    score = finalScore,
                    dateTime = System.currentTimeMillis(),
                    reason = reason
                )
            )
            _events.emit(GameEvent.NavigateToResult(finalScore, reason))
        }
    }

    /** Системная кнопка «назад» — показываем диалог выхода. */
    fun onBackPressed() {
        _uiState.update { it.copy(exitDialogVisible = true) }
    }

    /** Отмена диалога выхода. */
    fun dismissExitDialog() {
        _uiState.update { it.copy(exitDialogVisible = false) }
    }

    /** Подтверждение выхода без сохранения результата. */
    fun confirmExit() {
        viewModelScope.launch {
            _events.emit(GameEvent.NavigateBack)
        }
    }

    companion object {
        /** Фабрика ViewModel без DI: репозитории берём из QuizApplication. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as QuizApplication
                GameViewModel(
                    flagsRepository = app.flagsRepository,
                    statsRepository = app.statsRepository,
                    userPreferences = UserPreferences.getInstance(app),
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }
}