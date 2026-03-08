package io.github.garykam.budgetmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.garykam.budgetmate.data.local.dao.CreditCardDao
import io.github.garykam.budgetmate.data.local.dao.TransactionDao
import io.github.garykam.budgetmate.data.local.entity.CreditCard
import io.github.garykam.budgetmate.data.local.entity.Transaction

@Database(entities = [Transaction::class, CreditCard::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun creditCardDao(): CreditCardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "budget_mate_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
