package com.example.quizflags.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizflags.data.local.entity.FlagEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO для работы с таблицей флагов.
 */
@Dao
interface FlagDao {

    /** Все флаги в виде потока. */
    @Query("SELECT * FROM flags")
    fun getAll(): Flow<List<FlagEntity>>

    /** Флаг по идентификатору (может вернуть null). */
    @Query("SELECT * FROM flags WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): FlagEntity?

    /** Количество флагов в базе. */
    @Query("SELECT COUNT(*) FROM flags")
    suspend fun count(): Int

    /**
     * Вставка списка флагов (используется при первичном заполнении базы).
     * REPLACE гарантирует идемпотентность при повторной вставке.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(flags: List<FlagEntity>)
}