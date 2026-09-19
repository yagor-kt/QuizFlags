package com.example.quizflags.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Синглтон DataStore на уровне файла (top-level расширение).
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

/**
 * Обёртка над DataStore Preferences для хранения данных пользователя:
 * имя, статус авторизации и идентификатор гостя.
 */
class UserPreferences private constructor(private val context: Context) {

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        /** Возвращает единственный экземпляр [UserPreferences] (синглтон). */
        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                UserPreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    private object Keys {
        val USER_NAME = stringPreferencesKey("user_name")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val GUEST_ID = stringPreferencesKey("guest_id")
    }

    /** Поток с именем пользователя (по умолчанию пустая строка). */
    val userNameFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs -> prefs[Keys.USER_NAME] ?: "" }

    /** Поток со статусом авторизации (по умолчанию false). */
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs -> prefs[Keys.IS_LOGGED_IN] ?: false }

    /** Поток с идентификатором гостя (по умолчанию пустая строка). */
    val guestIdFlow: Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { prefs -> prefs[Keys.GUEST_ID] ?: "" }

    /** Сохранить имя пользователя. */
    suspend fun setUserName(name: String) {
        context.dataStore.edit { prefs -> prefs[Keys.USER_NAME] = name }
    }

    /** Установить статус авторизации. */
    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs -> prefs[Keys.IS_LOGGED_IN] = loggedIn }
    }

    /**
     * Если guestId пуст — генерируем «гость_» + 3 случайные цифры и сохраняем.
     * Если уже задан — ничего не меняем.
     */
    suspend fun ensureGuestId() {
        context.dataStore.edit { prefs ->
            val existing = prefs[Keys.GUEST_ID]
            if (existing.isNullOrEmpty()) {
                val randomDigits = (100..999).random().toString()
                prefs[Keys.GUEST_ID] = "гость_$randomDigits"
            }
        }
    }

    /**
     * Выход из аккаунта: очищаем имя и сбрасываем признак авторизации.
     * guest_id при этом НЕ трогаем.
     */
    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs.remove(Keys.USER_NAME)
            prefs[Keys.IS_LOGGED_IN] = false
        }
    }
}