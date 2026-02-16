package io.github.garykam.budgetmate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.garykam.budgetmate.ui.BudgetMateNavHost

@Composable
fun BudgetMateApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var topBarContent by remember { mutableStateOf<@Composable () -> Unit>({}) }

    Scaffold(
        topBar = {
            AnimatedVisibility(
                visible = currentRoute == "add_transaction",
                enter = slideInVertically(initialOffsetY = { -it }) + expandVertically() + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + shrinkVertically() + fadeOut()
            ) {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(title = { topBarContent() })
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute != "add_transaction",
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NavigationBar(navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentRoute != "add_transaction",
                enter = slideInVertically(initialOffsetY = { it * 2 }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it * 2 }) + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate("add_transaction") },
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay,
    ) { innerPadding ->
        val layoutDirection = LocalLayoutDirection.current
        val basePadding = PaddingValues(
            start = innerPadding.calculateStartPadding(layoutDirection),
            top = innerPadding.calculateTopPadding(),
            end = innerPadding.calculateEndPadding(layoutDirection),
            bottom = 0.dp
        )

        Box(modifier = Modifier.padding(basePadding)) {
            BudgetMateNavHost(
                navController = navController,
                onSetTopBar = { topBarContent = it },
                modifier = Modifier.padding(bottom = if (currentRoute != "add_transaction") 80.dp else 0.dp)
            )
        }
    }
}

@Composable
private fun NavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History") },
            selected = currentRoute == "history",
            onClick = {
                if (currentRoute != "history") {
                    navController.navigate("history") {
                        popUpTo("history") { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Analytics, contentDescription = "Stats") },
            label = { Text("Stats") },
            selected = currentRoute == "stats",
            onClick = {
                if (currentRoute != "stats") {
                    navController.navigate("stats") {
                        popUpTo("stats") { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == "settings",
            onClick = {
                if (currentRoute != "settings") {
                    navController.navigate("settings") {
                        popUpTo("settings") { inclusive = true }
                    }
                }
            }
        )

        NavigationBarItem(
            icon = {},
            label = {},
            selected = false,
            onClick = {}
        )
    }
}
