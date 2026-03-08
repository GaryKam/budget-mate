package io.github.garykam.budgetmate.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.garykam.budgetmate.data.local.AppDatabase
import io.github.garykam.budgetmate.data.local.dao.CreditCardDao
import io.github.garykam.budgetmate.data.local.dao.TransactionDao
import io.github.garykam.budgetmate.data.repository.CreditCardRepository
import io.github.garykam.budgetmate.data.repository.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCreditCardDao(database: AppDatabase): CreditCardDao {
        return database.creditCardDao()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(dao: TransactionDao): TransactionRepository {
        return TransactionRepository(dao)
    }

    @Provides
    @Singleton
    fun provideCreditCardRepository(dao: CreditCardDao): CreditCardRepository {
        return CreditCardRepository(dao)
    }
}
