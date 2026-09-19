package com.example.quizflags.navigation
/*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizflags.ui.about.AboutScreen
import com.example.quizflags.ui.flags.FlagsScreen
import com.example.quizflags.ui.flags.FlagsViewModel
import com.example.quizflags.ui.game.GameEvent
import com.example.quizflags.ui.game.GameScreen
import com.example.quizflags.ui.game.GameViewModel
import com.example.quizflags.ui.leaders.LeadersScreen
import com.example.quizflags.ui.leaders.LeadersViewModel
import com.example.quizflags.ui.login.LoginEvent
import com.example.quizflags.ui.login.LoginScreen
import com.example.quizflags.ui.login.LoginViewModel
import com.example.quizflags.ui.main.MainScreen
import com.example.quizflags.ui.result.ResultScreen
import com.example.quizflags.ui.result.ResultViewModel
import com.example.quizflags.ui.settings.SettingsScreen
import com.example.quizflags.ui.settings.SettingsViewModel
import com.example.quizflags.ui.stats.StatsScreen
import com.example.quizflags.ui.stats.StatsViewModel

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {

    NavHost(
        navController = navController,
        startDestination = Routes.Main.route
    ) {

        // ─── Главный экран (меню) ─────────────────────────────────────────────
        composable(Routes.Main.route) {
            MainScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        // ─── Игра ────────────────────────────────────────────────────────────
        composable(Routes.Game.route) {
            val gameViewModel: GameViewModel = viewModel(factory = GameViewModel.Factory)

            // Слушаем навигационные события из ViewModel
            LaunchedEffect(Unit) {
                gameViewModel.events.collect { event ->
                    when (event) {
                        is GameEvent.NavigateToResult -> {
                            navController.navigate(
                                Routes.Result.create(event.score, event.reason)
                            ) {
                                // Убираем игру из стека, чтобы нельзя было вернуться назад
                                popUpTo(Routes.Game.route) { inclusive = true }
                            }
                        }
                        GameEvent.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            GameScreen(viewModel = gameViewModel)
        }

        // ─── Результат ───────────────────────────────────────────────────────
        composable(
            route = Routes.Result.route,
            arguments = listOf(
                navArgument(Routes.Result.ARG_SCORE) { type = NavType.IntType },
                navArgument(Routes.Result.ARG_REASON) { type = NavType.StringType }
            )
        ) {
            val resultViewModel: ResultViewModel = viewModel(factory = ResultViewModel.Factory)
            ResultScreen(viewModel = resultViewModel)
        }

        // ─── Настройки ───────────────────────────────────────────────────────
        composable(Routes.Settings.route) {
            val settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
            SettingsScreen(viewModel = settingsViewModel)
        }

        // ─── Каталог флагов ──────────────────────────────────────────────────
        composable(Routes.Flags.route) {
            val flagsViewModel: FlagsViewModel = viewModel(factory = FlagsViewModel.Factory)
            FlagsScreen(viewModel = flagsViewModel)
        }

        // ─── Вход ────────────────────────────────────────────────────────────
        composable(Routes.Login.route) {
            val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)

            LaunchedEffect(Unit) {
                loginViewModel.events.collect { event ->
                    when (event) {
                        LoginEvent.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            LoginScreen(viewModel = loginViewModel)
        }

        // ─── Статистика ──────────────────────────────────────────────────────
        composable(Routes.Stats.route) {
            val statsViewModel: StatsViewModel = viewModel(factory = StatsViewModel.Factory)
            StatsScreen(viewModel = statsViewModel)
        }

        // ─── Лидеры ──────────────────────────────────────────────────────────
        composable(Routes.Leaders.route) {
            val leadersViewModel: LeadersViewModel = viewModel(factory = LeadersViewModel.Factory)
            LeadersScreen(viewModel = leadersViewModel)
        }

        // ─── О программе (статичный) ─────────────────────────────────────────
        composable(Routes.About.route) {
            AboutScreen()
        }
    }
}*/
