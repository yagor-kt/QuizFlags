package com.example.quizflags.data.local

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.quizflags.data.local.entity.FlagEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.json.JSONArray

/**
 * Колбэк, заполняющий таблицу флагов начальными данными из assets/flags.json
 * один раз — при первом создании базы данных.
 */
class FlagPrepopulateCallback : RoomDatabase.Callback() {

    // Скоуп уровня приложения для фоновой загрузки флагов.
    // GlobalScope не используется — у корутин контролируемый жизненный цикл.
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        val context = QuizDatabase.getAppContext()
        val database = QuizDatabase.getInstance(context)

        applicationScope.launch {
            val flags = readFlagsFromAssets(context)
            database.flagDao().insertAll(flags)
        }
    }

    /**
     * Читает flags.json из assets и преобразует его в список [FlagEntity].
     */
    private fun readFlagsFromAssets(context: Context): List<FlagEntity> {
        val json = context.assets.open("flags.json").bufferedReader().use { it.readText() }
        val array = JSONArray(json)

        return buildList {
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                add(
                    FlagEntity(
                        id = obj.getInt("id"),
                        name = obj.getString("name"),
                        imageName = obj.getString("imageName")
                    )
                )
            }
        }
    }
}