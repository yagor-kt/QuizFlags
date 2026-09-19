package com.example.quizflags.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность таблицы флагов стран.
 *
 * @property id        уникальный идентификатор страны.
 * @property name      название страны на русском языке.
 * @property imageName нижний регистр ISO-кода страны (например "ru", "us"),
 *                     используется для поиска файла вида ru.png в assets/flags/.
 */
@Entity(tableName = "flags")
data class FlagEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val imageName: String
)