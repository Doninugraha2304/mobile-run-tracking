package com.runtracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.runtracker.app.ui.screens.history.HistoryScreen
import com.runtracker.app.ui.screens.home.HomeScreen
import com.runtracker.app.ui.screens.running.RunningScreen
import com.runtracker.app.ui.screens.stats.StatsScreen

@Composable
fun RunTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onStartRun = { navController.navigate("running") },
                onHistory = { navController.navigate("history") },
                onStats = { navController.navigate("stats") }
            )
        }
        composable("running") {
            RunningScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("history") {
            HistoryScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable("stats") {
            StatsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
