package io.github.garykam.budgetmate.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.garykam.budgetmate.data.AppDatabase
import io.github.garykam.budgetmate.data.TransactionRepository
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
    @Singleton
    fun provideTransactionRepository(database: AppDatabase): TransactionRepository {
        return TransactionRepository(database)
    }
}
