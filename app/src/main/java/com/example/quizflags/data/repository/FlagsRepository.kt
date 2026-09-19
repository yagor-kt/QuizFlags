package com.example.quizflags.data.repository

import com.example.quizflags.data.local.dao.FlagDao
import com.example.quizflags.data.local.entity.FlagEntity
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий флагов — прослойка между ViewModel и базой данных.
 */
class FlagsRepository(private val flagDao: FlagDao) {

    /** Все флаги в виде потока. */
    fun getAllFlags(): Flow<List<FlagEntity>> = flagDao.getAll()

    /** Флаг по идентификатору. */
    suspend fun getFlagById(id: Int): FlagEntity? = flagDao.getById(id)

    /** Количество флагов в базе. */
    suspend fun getFlagsCount(): Int = flagDao.count()
}