package uz.toshmatov.currency.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "banks")
data class BanksEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("bank")
    val bank: String,
    @ColumnInfo("buy")
    val buy: String,
    @ColumnInfo("sell")
    val sell: String,
    @ColumnInfo("date")
    val date: String
)
