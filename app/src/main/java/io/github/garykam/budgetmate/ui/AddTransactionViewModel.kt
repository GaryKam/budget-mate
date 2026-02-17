package io.github.garykam.budgetmate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.Transaction
import io.github.garykam.budgetmate.data.TransactionRepository
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

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount.asStateFlow()

    fun onAmountChange(newValue: String) {
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
        val cents = _amount.value.toDoubleOrNull() ?: 0.0
        val finalAmount = cents / 100.0
        
        if (finalAmount > 0) {
            viewModelScope.launch {
                repository.insert(
                    Transaction(
                        name = "Manual Entry",
                        amount = finalAmount,
                        date = System.currentTimeMillis()
                    )
                )
                _amount.value = ""
                onComplete()
            }
        }
    }
}
