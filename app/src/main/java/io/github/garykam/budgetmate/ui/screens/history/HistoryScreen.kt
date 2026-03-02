package io.github.garykam.budgetmate.ui.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.garykam.budgetmate.data.local.entity.Transaction
import java.text.DateFormat
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val transactions by viewModel.uiState.collectAsState()
    
    // Define the heights for expanded and collapsed states
    val expandedHeight = 112.dp
    val collapsedHeight = 64.dp
    
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)

    val density = LocalDensity.current
    LaunchedEffect(density) {
        // Set the scroll limit to match our custom height difference
        scrollBehavior.state.heightOffsetLimit = with(density) { (collapsedHeight - expandedHeight).toPx() }
    }

    LaunchedEffect(scrollBehavior) {
        onSetTopBar {
            val fraction = scrollBehavior.state.collapsedFraction
            val currentHeight = expandedHeight + with(LocalDensity.current) { scrollBehavior.state.heightOffset.toDp() }

            // Interpolate text style between expanded and collapsed
            val titleStyle = lerp(
                MaterialTheme.typography.headlineMedium,
                MaterialTheme.typography.headlineSmall,
                fraction
            ).copy(fontWeight = FontWeight.Bold)

            // Interpolate background color
            val backgroundColor = lerp(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceDim,
                fraction
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(currentHeight),
                color = backgroundColor,
                tonalElevation = lerp(0.dp, 3.dp, fraction)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(TopAppBarDefaults.windowInsets)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "Transaction History",
                        style = titleStyle,
                        // Interpolate padding to fine-tune the position
                        modifier = Modifier.padding(bottom = lerp(16.dp, 12.dp, fraction))
                    )
                }
            }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            // Link the list scroll to the top bar behavior
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(transactions) { transaction ->
            TransactionItem(transaction = transaction)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Composable
private fun TransactionItem(transaction: Transaction) {
    val amountFormatter = NumberFormat.getCurrencyInstance(Locale.US)
    val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = transaction.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = dateFormatter.format(Date(transaction.date)),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = amountFormatter.format(transaction.amount),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
