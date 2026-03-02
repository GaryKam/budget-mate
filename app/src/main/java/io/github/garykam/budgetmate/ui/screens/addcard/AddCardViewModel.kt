package io.github.garykam.budgetmate.ui.screens.addcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _digits = MutableStateFlow("")
    val digits: StateFlow<String> = _digits.asStateFlow()

    private val _selectedOption = MutableStateFlow("Chase")
    val selectedOption: StateFlow<String> = _selectedOption.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun onNameChange(name: String) {
        if (!isSaving.value) {
            _name.value = name
        }
    }

    fun onDigitChange(digits: String) {
        if (!isSaving.value) {
            _digits.value = digits
        }
    }

    fun onOptionSelected(option: String) {
        if (!isSaving.value) {
            _selectedOption.value = option
        }
    }

    fun addCard(onComplete: () -> Unit) {
        if (_isSaving.value) {
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            delay(1000L)

            onComplete()
            _isSaving.value = false
        }
    }
}
