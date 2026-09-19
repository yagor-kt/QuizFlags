package com.example.quizflags.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quizflags.data.local.LeaderRow
import com.example.quizflags.data.local.entity.StatEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с таблицей статистики.
 */
@Dao
interface StatsDao {

    /** Сохранить результат партии. */
    @Insert
    suspend fun insert(stat: StatEntity)

    /** Вся статистика, отсортированная от новых к старым. */
    @Query("SELECT * FROM statistics ORDER BY dateTime DESC")
    fun getAll(): Flow<List<StatEntity>>

    /**
     * Таблица лидеров: лучший результат каждого игрока,
     * отсортированный по убыванию, топ-10.
     */
    @Query(
        "SELECT userName, MAX(score) AS best " +
                "FROM statistics " +
                "GROUP BY userName " +
                "ORDER BY best DESC LIMIT 10"
    )
    fun getLeaders(): Flow<List<LeaderRow>>

    /** Полностью очистить статистику. */
    @Query("DELETE FROM statistics")
    suspend fun clearAll()
}