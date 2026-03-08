package io.github.garykam.budgetmate.data.repository

import io.github.garykam.budgetmate.data.local.dao.CreditCardDao
import io.github.garykam.budgetmate.data.local.entity.CreditCard
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreditCardRepository @Inject constructor(
    private val creditCardDao: CreditCardDao
) {
    val creditCards: Flow<List<CreditCard>> = creditCardDao.getCreditCards()

    suspend fun addCreditCard(name: String, brand: String? = null) {
        val creditCard = CreditCard(name = name, brand = brand)
        creditCardDao.insertCreditCard(creditCard)
    }

    suspend fun deleteCreditCard(id: Int) {
        creditCardDao.deleteCreditCard(id)
    }
}
