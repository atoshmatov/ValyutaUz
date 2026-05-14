package uz.toshmatov.currency.data.local.room.entity

import androidx.room.Entity

@Entity(tableName = "currency_history", primaryKeys = ["currencyCode", "date"])
data class CurrencyHistoryEntity(
    val currencyCode: String,
    val date: String,
    val rate: Float
)
