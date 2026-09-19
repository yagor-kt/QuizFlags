package com.example.quizflags.data.repository

import com.example.quizflags.data.local.LeaderRow
import com.example.quizflags.data.local.dao.StatsDao
import com.example.quizflags.data.local.entity.StatEntity
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий статистики — прослойка между ViewModel и базой данных.
 */
class StatsRepository(private val statsDao: StatsDao) {

    /** Сохранить результат партии. */
    suspend fun insertStat(stat: StatEntity) = statsDao.insert(stat)

    /** Вся статистика (от новых к старым). */
    fun getAllStats(): Flow<List<StatEntity>> = statsDao.getAll()

    /** Топ-10 лидеров. */
    fun getLeaders(): Flow<List<LeaderRow>> = statsDao.getLeaders()

    /** Очистить статистику. */
    suspend fun clearAll() = statsDao.clearAll()
}