package uz.toshmatov.currency.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cbu")
data class CBUEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: String,
    @ColumnInfo("Ccy")
    val currencyCode: String,
    @ColumnInfo("CcyNm_RU")
    val currencyNameRU: String,
    @ColumnInfo("CcyNm_UZ")
    val currencyNameUZ: String,
    @ColumnInfo("CcyNm_UZC")
    val currencyNameUZC: String,
    @ColumnInfo("CcyNm_EN")
    val currencyNameEN: String,
    @ColumnInfo("Nominal")
    val nominal: String,
    @ColumnInfo("Rate")
    val rate: String,
    @ColumnInfo("Diff")
    val difference: String,
    @ColumnInfo("Date")
    val date: String
)
