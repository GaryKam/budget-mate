package io.github.garykam.budgetmate.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.garykam.budgetmate.ui.screens.addcard.AddCardScreen
import io.github.garykam.budgetmate.ui.screens.addtransaction.AddTransactionScreen
import io.github.garykam.budgetmate.ui.screens.history.HistoryScreen
import io.github.garykam.budgetmate.ui.screens.settings.SettingsScreen
import io.github.garykam.budgetmate.ui.screens.stats.StatsScreen

@Composable
fun AppNavHost(
    rootNavController: NavHostController = rememberNavController()
) {
    // Wrap root NavHost in a Surface to prevent white flash during transitions in dark theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = "main_tabs"
        ) {
            composable("main_tabs") {
                MainScaffold(rootNavController = rootNavController)
            }

            composable(
                route = "add_transaction",
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                AddTransactionScreen(
                    onBack = { rootNavController.popBackStack() }
                )
            }

            composable(
                route = "add_card",
                enterTransition = { slideInVertically(initialOffsetY = { it }) },
                exitTransition = { slideOutVertically(targetOffsetY = { it }) }
            ) {
                AddCardScreen(
                    onBack = { rootNavController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun MainScaffold(rootNavController: NavHostController) {
    val navController = rememberNavController()
    var topBarContent by remember { mutableStateOf<@Composable () -> Unit>({}) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            topBar = {
                AnimatedContent(
                    targetState = topBarContent,
                    transitionSpec = {
                        slideInVertically { height -> -height } + fadeIn() togetherWith
                                slideOutVertically { height -> -height } + fadeOut()
                    },
                    label = "MainScaffoldTopBarAnimation"
                ) { targetContent ->
                    targetContent()
                }
            },
            bottomBar = {
                MainNavigationBar(navController)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { rootNavController.navigate("add_transaction") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Transaction")
                }
            },
            floatingActionButtonPosition = FabPosition.EndOverlay,
        ) { innerPadding ->
            val layoutDirection = LocalLayoutDirection.current
            val padding = PaddingValues(
                start = innerPadding.calculateStartPadding(layoutDirection),
                top = innerPadding.calculateTopPadding(),
                end = innerPadding.calculateEndPadding(layoutDirection),
                bottom = 0.dp
            )

            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                NavHost(
                    navController = navController,
                    startDestination = "stats",
                    modifier = Modifier.padding(bottom = 80.dp) // Space for bottom bar
                ) {
                    composable("history") {
                        HistoryScreen(onSetTopBar = { topBarContent = it })
                    }
                    composable("stats") {
                        StatsScreen(onSetTopBar = { topBarContent = it })
                    }
                    composable("settings") {
                        SettingsScreen(
                            onSetTopBar = { topBarContent = it },
                            onNavigateToAddCard = { rootNavController.navigate("add_card") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainNavigationBar(navController: NavController) {
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
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
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
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        // Empty item for FAB spacing
        NavigationBarItem(
            icon = {},
            label = {},
            selected = false,
            onClick = {},
            enabled = false
        )
    }
}
