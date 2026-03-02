package io.github.garykam.budgetmate.ui.screens.addcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBack: () -> Unit,
    viewModel: AddCardViewModel = hiltViewModel()
) {
    val name by viewModel.name.collectAsState()
    val digits by viewModel.digits.collectAsState()
    val focusManager = LocalFocusManager.current
    var isDropdownOpen by remember { mutableStateOf(false) }
    val dropdownOptions = listOf("Chase", "Amex", "Discover")
    val selectedOption by viewModel.selectedOption.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add credit card") },
                navigationIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        onBack()
                    }) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "Cancel")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        viewModel.addCard(onComplete = {
                            focusManager.clearFocus()
                            onBack()

                        })
                    }) {
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
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 32.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { viewModel.onNameChange(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Name") }
                    )

                    Spacer(modifier = Modifier.height(80.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Spacer(modifier = Modifier.weight(0.2f))

                        OutlinedTextField(
                            value = digits,
                            onValueChange = { viewModel.onDigitChange(it) },
                            modifier = Modifier.weight(0.3f),
                            placeholder = { Text("****") },
                            supportingText = { Text("Last 4 digits") }
                        )

                        Spacer(modifier = Modifier.weight(0.1f))

                        ExposedDropdownMenuBox(
                            expanded = isDropdownOpen,
                            onExpandedChange = { isDropdownOpen = it },
                            modifier = Modifier.weight(0.4f)
                        ) {
                            OutlinedTextField(
                                value = selectedOption,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownOpen)
                                },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                supportingText = { Text("Brand") },
                                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            )

                            ExposedDropdownMenu(
                                expanded = isDropdownOpen,
                                onDismissRequest = { isDropdownOpen = false }
                            ) {
                                dropdownOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            viewModel.onOptionSelected(selectionOption)
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
}
