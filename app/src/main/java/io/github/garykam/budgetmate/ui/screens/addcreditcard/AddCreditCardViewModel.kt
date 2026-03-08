package io.github.garykam.budgetmate.ui.screens.addcreditcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.garykam.budgetmate.data.local.model.CreditCardBrand
import io.github.garykam.budgetmate.data.repository.CreditCardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCreditCardViewModel @Inject constructor(
    private val repository: CreditCardRepository
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _selectedBrand: MutableStateFlow<CreditCardBrand?> = MutableStateFlow(null)
    val selectedBrand: StateFlow<CreditCardBrand?> = _selectedBrand.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _isDirty = MutableStateFlow(false)
    val isDirty: StateFlow<Boolean> = _isDirty.asStateFlow()

    fun onNameChange(name: String) {
        if (!isSaving.value) {
            _name.value = name
            _isDirty.value = name.isNotBlank()
        }
    }

    fun onBrandChange(brand: CreditCardBrand?) {
        if (!isSaving.value) {
            _selectedBrand.value = brand
        }
    }

    fun addCard(onComplete: () -> Unit) {
        if (_isSaving.value) {
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            delay(1000L)

            try {
                repository.addCreditCard(name = _name.value, brand = _selectedBrand.value?.displayName)
                onComplete()
            } catch (exception: Exception) {
                // TODO
            } finally {
                _isSaving.value = false
            }
        }
    }
}
