package io.github.garykam.budgetmate.ui.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun StatsScreen(
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val totalCost by viewModel.totalCost.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    LaunchedEffect(Unit) {
        onSetTopBar {}
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ElevatedCard {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${currencyFormatter.format(totalCost)}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 32.dp)
                )
                Text(
                    text = "Total Spending",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        ElevatedCard {
            Box(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "${transactions.size}",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 32.dp)
                )
                Text(
                    text = "Transactions",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
