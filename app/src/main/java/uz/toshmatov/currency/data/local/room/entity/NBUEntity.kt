package uz.toshmatov.currency.data.local.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nbu")
data class NBUEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("code")
    val code: String,
    @ColumnInfo("cb_price")
    val cbPrice: String,
    @ColumnInfo("nbu_buy_price")
    val nbuBuyPrice: String,
    @ColumnInfo("nbu_cell_price")
    val nbuCellPrice: String,
    @ColumnInfo("date")
    val date: String
)
