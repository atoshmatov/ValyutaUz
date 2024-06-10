package uz.toshmatov.currency.domain.model

import androidx.compose.runtime.Stable

@Stable
data class NBUModel(
    val id: Int = 0,
    val title: String,
    val code: String,
    val cbPrice: String,
    val nbuBuyPrice: String,
    val nbuCellPrice: String,
    val date: String
)