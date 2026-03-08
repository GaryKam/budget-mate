package io.github.garykam.budgetmate.ui.screens.addtransaction

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.garykam.budgetmate.ui.util.CurrencyVisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddTransactionViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val amount by viewModel.amount.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isDirty by viewModel.isDirty.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var showErrors by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    fun handleBack() {
        if (!isSaving) {
            if (isDirty) {
                showExitDialog = true
            } else {
                onBack()
            }
        }
    }

    BackHandler(enabled = true) {
        handleBack()
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to exit?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onBack()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            handleBack()
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "New transaction",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = name,
                onValueChange = {
                    viewModel.onNameChange(it)
                    if (showErrors && it.isNotBlank()) {
                        showErrors = false
                    }
                },
                modifier = Modifier
                    .width(280.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isTextFieldFocused = it.isFocused },
                label = { Text("Name") },
                isError = showErrors && name.isBlank(),
                supportingText = {
                    if (showErrors && name.isBlank()) {
                        Text("A name is required")
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    viewModel.onAmountChange(it)
                    if (showErrors && it.isNotEmpty()) {
                        showErrors = false
                    }
                },
                modifier = Modifier
                    .width(280.dp)
                    .onFocusChanged { isTextFieldFocused = it.isFocused },
                label = { Text("Amount") },
                isError = showErrors && amount.isEmpty(),
                supportingText = {
                    if (showErrors && amount.isEmpty()) {
                        Text("An amount is required")
                    }
                },
                prefix = { Text("$ ") },
                visualTransformation = CurrencyVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        showErrors = true
                        focusManager.clearFocus()
                    }
                ),
                singleLine = true
            )

            AnimatedVisibility(visible = !isTextFieldFocused && name.isNotBlank() && amount.isNotEmpty()) {
                Button(
                    onClick = { viewModel.addTransaction(onComplete = onBack) },
                    enabled = !isSaving,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .defaultMinSize(minWidth = 150.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .sizeIn(24.dp, 24.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                style = LocalTextStyle.current.copy(
                                    platformStyle = PlatformTextStyle(includeFontPadding = false),
                                    lineHeightStyle = LineHeightStyle(
                                        alignment = LineHeightStyle.Alignment.Center,
                                        trim = LineHeightStyle.Trim.Both
                                    )
                                )
                            )
                        }
                        Text(
                            text = "Add $ ${viewModel.formatAmount(amount)}",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }
    }
}
