package io.github.garykam.budgetmate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.garykam.budgetmate.data.local.AppDatabase
import io.github.garykam.budgetmate.data.repository.CreditCardRepository
import io.github.garykam.budgetmate.data.repository.TransactionRepository

@HiltAndroidApp
class BudgetMateApp : Application() {
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val transactionRepository by lazy { TransactionRepository(database.transactionDao()) }
    private val creditCardRepository by lazy { CreditCardRepository(database.creditCardDao()) }
}
