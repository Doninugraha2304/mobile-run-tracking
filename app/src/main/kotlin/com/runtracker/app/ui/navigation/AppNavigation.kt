package com.runtracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.runtracker.app.ui.screens.history.HistoryScreen
import com.runtracker.app.ui.screens.home.HomeScreen
import com.runtracker.app.ui.screens.interval.IntervalScreen
import com.runtracker.app.ui.screens.running.RunningScreen
import com.runtracker.app.ui.screens.settings.SettingsScreen
import com.runtracker.app.ui.screens.stats.StatsScreen

@Composable
fun RunTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onStartRun = { navController.navigate("running") },
                onStartInterval = { navController.navigate("interval") },
                onHistory = { navController.navigate("history") },
                onStats = { navController.navigate("stats") },
                onSettings = { navController.navigate("settings") }
            )
        }
        composable("running") {
            RunningScreen(onBack = { navController.popBackStack() })
        }
        composable(
            "running_interval/{runSec}/{walkSec}/{sets}",
            arguments = listOf(
                navArgument("runSec") { type = NavType.IntType },
                navArgument("walkSec") { type = NavType.IntType },
                navArgument("sets") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val runSec = backStackEntry.arguments?.getInt("runSec") ?: 5
            val walkSec = backStackEntry.arguments?.getInt("walkSec") ?: 3
            val sets = backStackEntry.arguments?.getInt("sets") ?: 8
            RunningScreen(
                onBack = { navController.popBackStack() },
                isInterval = true,
                intervalRunSec = runSec,
                intervalWalkSec = walkSec,
                intervalSets = sets
            )
        }
        composable("interval") {
            IntervalScreen(
                onBack = { navController.popBackStack() },
                onStartInterval = { run, walk, sets ->
                    navController.navigate("running_interval/$run/$walk/$sets")
                }
            )
        }
        composable("history") {
            HistoryScreen(onBack = { navController.popBackStack() })
        }
        composable("stats") {
            StatsScreen(onBack = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
