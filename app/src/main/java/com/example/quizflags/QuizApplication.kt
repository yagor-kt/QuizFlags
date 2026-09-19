package com.example.quizflags

import android.app.Application
import com.example.quizflags.data.local.QuizDatabase
import com.example.quizflags.data.prefs.UserPreferences
import com.example.quizflags.data.repository.FlagsRepository
import com.example.quizflags.data.repository.StatsRepository

class QuizApplication : Application() {
    /** Ленивая инициализация БД — создастся при первом обращении. */
    val database: QuizDatabase by lazy {
        QuizDatabase.getInstance(this)
    }

    /** Репозиторий флагов. */
    val flagsRepository: FlagsRepository by lazy {
        FlagsRepository(database.flagDao())
    }

    /** Репозиторий статистики. */
    val statsRepository: StatsRepository by lazy {
        StatsRepository(database.statsDao())
    }

    /** DataStore для настроек пользователя. */
    val userPreferences: UserPreferences by lazy {
        UserPreferences.getInstance(this)
    }
}