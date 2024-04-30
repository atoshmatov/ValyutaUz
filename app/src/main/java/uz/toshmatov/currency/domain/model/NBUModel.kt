package uz.toshmatov.currency.domain.model

import androidx.compose.runtime.Stable

@Stable
data class NBUModel(
    val title: String,
    val code: String,
    val cbPrice: String,
    val nbuBuyPrice: String,
    val nbCellPrice: String,
    val date: String
)