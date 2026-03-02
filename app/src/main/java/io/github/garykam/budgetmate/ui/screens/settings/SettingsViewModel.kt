package io.github.garykam.budgetmate.ui.screens.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.repository.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    fun addCard() {
        // Add card logic here
    }

    fun removeCard() {
        // Remove card logic here
    }
}
