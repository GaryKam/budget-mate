package io.github.garykam.budgetmate.ui.screens.addcard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.garykam.budgetmate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    viewModel: AddCardViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val isDirty by viewModel.isDirty.collectAsState()
    val focusManager = LocalFocusManager.current
    val dropdownOptions = listOf("Chase", "Amex", "Discover")
    var isDropdownOpen by remember { mutableStateOf(false) }
    var showExitConfirmation by remember { mutableStateOf(false) }

    fun handleBack() {
        if (isDirty) {
            showExitConfirmation = true
        } else {
            onBack()
        }
    }

    BackHandler(enabled = true) {
        handleBack()
    }

    if (showExitConfirmation) {
        AlertDialog(
            onDismissRequest = { showExitConfirmation = false },
            title = { Text("Discard changes?") },
            text = { Text("You have unsaved changes. Are you sure you want to exit?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitConfirmation = false
                        onBack()
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add credit card") },
                navigationIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        handleBack()
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(
                        enabled = isDirty,
                        onClick = {
                            viewModel.addCard(
                                onComplete = {
                                    focusManager.clearFocus()
                                    onBack()
                                }
                            )
                        }
                    ) {
                        Text("Save")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.onNameChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Name") },
                        supportingText = { Text("Nickname for the card") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(80.dp))

                    ExposedDropdownMenuBox(
                        expanded = isDropdownOpen,
                        onExpandedChange = { isDropdownOpen = it },
                        modifier = Modifier
                            .width(180.dp)
                            .align(Alignment.End)
                    ) {
                        OutlinedTextField(
                            value = selectedOption,
                            onValueChange = {},
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            readOnly = true,
                            leadingIcon = {
                                val iconRes = when (selectedOption) {
                                    "Chase" -> painterResource(R.drawable.ic_chase)
                                    "Amex" -> painterResource(R.drawable.ic_amex)
                                    "Discover" -> painterResource(R.drawable.ic_discover)
                                    else -> null
                                }
                                if (iconRes != null) {
                                    Icon(painter = iconRes, contentDescription = "null")
                                }
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownOpen) },
                            supportingText = { Text("Brand") },
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        )

                        ExposedDropdownMenu(
                            expanded = isDropdownOpen,
                            onDismissRequest = { isDropdownOpen = false }
                        ) {
                            dropdownOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        viewModel.onOptionSelected(option)
                                        isDropdownOpen = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
