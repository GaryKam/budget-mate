package io.github.garykam.budgetmate.data

import kotlinx.coroutines.flow.Flow

class TransactionRepository(private val database: AppDatabase) {
    private val transactionDao = database.transactionDao()

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAll()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        transactionDao.delete(transaction)
    }
}
