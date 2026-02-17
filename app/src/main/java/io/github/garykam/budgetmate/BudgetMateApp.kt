package io.github.garykam.budgetmate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.garykam.budgetmate.data.AppDatabase
import io.github.garykam.budgetmate.data.TransactionRepository

@HiltAndroidApp
class BudgetMateApp : Application() {
    private val database by lazy { AppDatabase.getInstance(applicationContext) }
    private val repository by lazy { TransactionRepository(database) }
}
