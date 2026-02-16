package io.github.garykam.budgetmate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HistoryScreen(onSetTopBar: (@Composable () -> Unit) -> Unit) {
    LaunchedEffect(Unit) {
        onSetTopBar {}
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "History")
    }
}
