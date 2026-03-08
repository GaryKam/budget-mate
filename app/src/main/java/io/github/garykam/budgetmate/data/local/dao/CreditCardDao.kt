package io.github.garykam.budgetmate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.garykam.budgetmate.data.local.entity.CreditCard
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {
    @Query("SELECT * FROM credit_cards")
    fun getCreditCards(): Flow<List<CreditCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreditCard(creditCard: CreditCard)

    @Query("DELETE FROM credit_cards WHERE id = :id")
    suspend fun deleteCreditCard(id: Int)
}
