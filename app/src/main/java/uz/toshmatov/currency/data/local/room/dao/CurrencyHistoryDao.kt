package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.toshmatov.currency.data.local.room.entity.CurrencyHistoryEntity

@Dao
interface CurrencyHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CurrencyHistoryEntity>)

    @Query("SELECT * FROM currency_history WHERE currencyCode = :code ORDER BY date ASC")
    suspend fun getHistory(code: String): List<CurrencyHistoryEntity>

    @Query("SELECT date FROM currency_history WHERE currencyCode = :code")
    suspend fun getCachedDates(code: String): List<String>
}
