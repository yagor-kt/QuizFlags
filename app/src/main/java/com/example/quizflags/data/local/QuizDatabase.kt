package com.example.quizflags.data.local


import android.annotation.SuppressLint
import android.content.Context
import android.content.MutableContextWrapper
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizflags.data.local.dao.FlagDao
import com.example.quizflags.data.local.dao.StatsDao
import com.example.quizflags.data.local.entity.FlagEntity
import com.example.quizflags.data.local.entity.StatEntity

/**
 * База данных приложения (Room).
 * Хранит флаги стран и статистику партий.
 */
@Database(
    entities = [FlagEntity::class, StatEntity::class],
    version = 1,
    exportSchema = false
)
abstract class QuizDatabase : RoomDatabase() {

    abstract fun flagDao(): FlagDao
    abstract fun statsDao(): StatsDao

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: QuizDatabase? = null

        // Обёртка, чтобы хранить только applicationContext и не допускать утечек активности.
        @SuppressLint("StaticFieldLeak")
        private val appContext = MutableContextWrapper(null)

        /**
         * Возвращает единственный экземпляр базы (синглтон).
         */
        fun getInstance(context: Context): QuizDatabase {
            appContext.baseContext = context.applicationContext
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext.baseContext!!,
                    QuizDatabase::class.java,
                    "quiz_flags.db"
                )
                    .addCallback(FlagPrepopulateCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /** Доступ к applicationContext (используется в callback'е предзаполнения). */
        fun getAppContext(): Context = appContext.baseContext!!
    }
}