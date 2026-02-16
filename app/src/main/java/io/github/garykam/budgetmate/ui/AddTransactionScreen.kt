package io.github.garykam.budgetmate.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AddTransactionScreen(
    onSetTopBar: (@Composable () -> Unit) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var transactionAmount by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onSetTopBar {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }

        // Focus the text field
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = transactionAmount,
            onValueChange = { newValue ->
                val oldDigits = transactionAmount.text.filter { it.isDigit() }
                val newDigits = newValue.text.filter { it.isDigit() }

                val finalDigits = when {
                    newDigits.length > oldDigits.length -> {
                        val charAdded = newValue.text.getOrNull(newValue.selection.start - 1)
                        // Added 1 or pasted multiple chars
                        if (charAdded?.isDigit() == true) {
                            oldDigits + charAdded
                        } else {
                            newDigits
                        }
                    }
                    // Removed 1 char
                    newDigits.length < oldDigits.length -> {
                        if (oldDigits.isNotEmpty()) oldDigits.dropLast(1) else ""
                    }

                    else -> oldDigits
                }

                if (finalDigits.isEmpty()) {
                    transactionAmount = TextFieldValue("")
                } else {
                    val cents = finalDigits.toDoubleOrNull() ?: 0.0
                    val totalAmount = cents / 100.0
                    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
                        minimumFractionDigits = 2
                        maximumFractionDigits = 2
                    }
                    val formattedAmount = formatter.format(totalAmount)
                    transactionAmount = TextFieldValue(
                        text = formattedAmount,
                        selection = TextRange(formattedAmount.length)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { isTextFieldFocused = it.isFocused },
            label = { Text("Amount") },
            prefix = { Text("$ ") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            singleLine = true
        )

        AnimatedVisibility(visible = !isTextFieldFocused) {
            val amount = transactionAmount.text.toDoubleOrNull() ?: 0.0
            Button(onClick = {}) {
                Text(
                    text = if (amount > 0) "+" else "-",
                    modifier = Modifier.defaultMinSize(20.dp).background(MaterialTheme.colorScheme.onPrimary, CircleShape),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "$ $amount",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
