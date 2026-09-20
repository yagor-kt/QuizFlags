package com.example.quizflags.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quizflags.ui.about.AboutScreen
import com.example.quizflags.ui.flags.FlagsListScreen
import com.example.quizflags.ui.game.GameScreen
import com.example.quizflags.ui.leaders.LeadersScreen
import com.example.quizflags.ui.login.LoginScreen
import com.example.quizflags.ui.main.MainScreen
import com.example.quizflags.ui.result.ResultScreen
import com.example.quizflags.ui.settings.SettingsScreen
import com.example.quizflags.ui.stats.StatsScreen
import com.example.quizflags.ui.flags.FlagsViewModel
import com.example.quizflags.ui.game.GameEvent
import com.example.quizflags.ui.game.GameViewModel
import com.example.quizflags.ui.leaders.LeadersViewModel
import com.example.quizflags.ui.login.LoginViewModel
import com.example.quizflags.ui.settings.SettingsViewModel
import com.example.quizflags.ui.stats.StatsViewModel

// Граф навигации: связывает маршруты Routes с экранами и ViewModel'ами.
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Main.route,
    ) {
        composable(Routes.Main.route) {
            MainScreen(
                onNavigate = { path -> navController.navigate(path) }
            )
        }

        composable(Routes.Game.route) {
            val vm: GameViewModel = viewModel(factory = GameViewModel.Factory)

            LaunchedEffect(Unit) {
                vm.events.collect { event ->
                    when (event) {
                        is GameEvent.NavigateToResult -> {
                            navController.navigate(Routes.Result.create(event.score, event.reason)) {
                                popUpTo(Routes.Game.route) { inclusive = true }
                            }
                        }
                        GameEvent.NavigateBack -> navController.popBackStack()
                    }
                }
            }

            GameScreen(vm = vm)
        }

        composable(Routes.Settings.route) {
            val vm: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory)
            SettingsScreen(
                vm = vm,
                onNavigate = { route -> navController.navigate(route) },
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(Routes.Main.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.Flags.route) {
            val vm: FlagsViewModel = viewModel(factory = FlagsViewModel.Factory)
            FlagsListScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Login.route) {
            val vm: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
            LoginScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.Stats.route) {
            val vm: StatsViewModel = viewModel(factory = StatsViewModel.Factory)
            StatsScreen(
                vm = vm,
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.Leaders.route) {
            val vm: LeadersViewModel = viewModel(factory = LeadersViewModel.Factory)
            LeadersScreen(
                vm = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.Result.route,
            arguments = listOf(
                navArgument(Routes.Result.ARG_SCORE) { type = NavType.IntType },
                navArgument(Routes.Result.ARG_REASON) { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt(Routes.Result.ARG_SCORE) ?: 0
            val reason = backStackEntry.arguments?.getString(Routes.Result.ARG_REASON).orEmpty()
            ResultScreen(
                score = score,
                reason = reason,
                onRestart = {
                    navController.navigate(Routes.Game.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToMain = {
                    navController.navigate(Routes.Main.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}
