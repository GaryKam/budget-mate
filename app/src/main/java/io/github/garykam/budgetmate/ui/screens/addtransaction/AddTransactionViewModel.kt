package io.github.garykam.budgetmate.ui.screens.addtransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.local.entity.Transaction
import io.github.garykam.budgetmate.data.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun onNameChange(newValue: String) {
        if (_isSaving.value) {
            return
        }
        _name.value = newValue
    }

    fun onAmountChange(newValue: String) {
        if (_isSaving.value) {
            return
        }
        val digits = newValue.filter { it.isDigit() }
        if (digits.length <= 9) {
            _amount.value = digits
        }
    }

    fun formatAmount(digits: String): String {
        if (digits.isEmpty()) return "0.00"
        val cents = digits.toDoubleOrNull() ?: 0.0
        val totalAmount = cents / 100.0
        val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return formatter.format(totalAmount)
    }

    fun addTransaction(onComplete: () -> Unit) {
        if (_isSaving.value) {
            return
        }

        val cents = _amount.value.toDoubleOrNull() ?: 0.0
        val totalAmount = cents / 100.0

        viewModelScope.launch {
            _isSaving.value = true
            delay(1000L)

            repository.insert(
                Transaction(
                    name = _name.value,
                    amount = totalAmount,
                    date = System.currentTimeMillis()
                )
            )
            onComplete()
        }

    }
}
