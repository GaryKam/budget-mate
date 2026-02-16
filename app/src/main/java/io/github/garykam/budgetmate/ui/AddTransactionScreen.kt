package io.github.garykam.budgetmate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTransactionScreen(
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        onSetTopBar { Text("New Transaction") }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Transaction Content Here")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}
