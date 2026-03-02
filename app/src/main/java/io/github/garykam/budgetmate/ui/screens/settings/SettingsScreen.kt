package io.github.garykam.budgetmate.ui.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    onNavigateToAddCard: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        onSetTopBar {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Settings") },
                actions = {
                    IconButton(onClick = onNavigateToAddCard) {
                        Icon(imageVector = Icons.Filled.AddCard, contentDescription = "Navigate To Add Card")
                    }
                }
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings Screen Content")
    }
}
