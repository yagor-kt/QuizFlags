package com.example.quizflags.navigation

/**
 * Маршруты приложения.
 */
sealed class Routes(val route: String) {
    object Main : Routes("main")
    object Game : Routes("game")
    object Settings : Routes("settings")
    object Flags : Routes("flags")
    object Login : Routes("login")
    object Stats : Routes("stats")
    object Leaders : Routes("leaders")
    object About : Routes("about")

    object Result : Routes("result/{score}/{reason}") {
        const val ARG_SCORE = "score"
        const val ARG_REASON = "reason"

        /** Строит конкретный маршрут с подставленными значениями. */
        fun create(score: Int, reason: String): String = "result/$score/$reason"
    }
}