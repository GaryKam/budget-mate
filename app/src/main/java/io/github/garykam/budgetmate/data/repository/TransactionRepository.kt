package io.github.garykam.budgetmate.data.repository

import io.github.garykam.budgetmate.data.local.AppDatabase
import io.github.garykam.budgetmate.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val database: AppDatabase) {
    private val transactionDao = database.transactionDao()

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }
}
