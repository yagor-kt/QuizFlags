package com.example.quizflags.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность таблицы статистики сыгранных партий.
 *
 * @property reason причина окончания игры:
 *                  "lives" — жизни закончились,
 *                  "time"  — минута истекла.
 */
@Entity(tableName = "statistics")
data class StatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userName: String,
    val score: Int,
    val dateTime: Long,
    val reason: String
)