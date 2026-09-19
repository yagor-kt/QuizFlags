package com.example.quizflags.data.local

/**
 * Строка результата для таблицы лидеров.
 *
 * @property userName имя игрока.
 * @property best     его лучший (максимальный) результат.
 */
data class LeaderRow(
    val userName: String,
    val best: Int
)