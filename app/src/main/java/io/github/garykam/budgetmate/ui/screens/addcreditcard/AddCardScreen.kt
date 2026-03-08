package io.github.garykam.budgetmate.ui.screens.addcreditcard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import io.github.garykam.budgetmate.data.local.model.CardBrand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    viewModel: AddCreditCardViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val selectedBrand by viewModel.selectedBrand.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val isDirty by viewModel.isDirty.collectAsState()
    val focusManager = LocalFocusManager.current
    var isDropdownOpen by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

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
                        enabled = isDirty && !isSaving,
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.addCard(
                                onComplete = {
                                    onBack()
                                }
                            )
                        }
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text("Save")
                        }
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
                            value = selectedBrand?.displayName ?: "",
                            onValueChange = {},
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            readOnly = true,
                            leadingIcon = {
                                selectedBrand?.iconRes?.let {
                                    Icon(painter = painterResource(it), contentDescription = "null")
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
                            CardBrand.entries.forEach { brand ->
                                DropdownMenuItem(
                                    text = { Text(brand.displayName) },
                                    onClick = {
                                        viewModel.onBrandChange(CardBrand.fromString(brand.displayName))
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
