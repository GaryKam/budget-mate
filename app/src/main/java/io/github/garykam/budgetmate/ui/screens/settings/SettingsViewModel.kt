package io.github.garykam.budgetmate.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.local.entity.CreditCard
import io.github.garykam.budgetmate.data.repository.CreditCardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: CreditCardRepository
) : ViewModel() {
    val creditCards: StateFlow<List<CreditCard>> = repository.creditCards
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun removeCreditCard() {
        // repository.deleteCreditCard()
    }
}
