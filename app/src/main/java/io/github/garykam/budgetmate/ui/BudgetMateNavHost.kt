package io.github.garykam.budgetmate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BudgetMateNavHost(
    navController: NavHostController,
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "stats",
        modifier = modifier
    ) {
        composable("history") {
            HistoryScreen(onSetTopBar = onSetTopBar)
        }

        composable("stats") {
            StatsScreen(onSetTopBar = onSetTopBar)
        }

        composable("settings") {
            SettingsScreen(onSetTopBar = onSetTopBar)
        }

        composable("add_transaction") {
            AddTransactionScreen(
                onSetTopBar = onSetTopBar,
                onBack = { navController.popBackStack() },
                modifier = modifier
            )
        }
    }
}
