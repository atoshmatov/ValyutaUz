package uz.toshmatov.currency.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ExchangeModel(
    val bank: String,
    val buy: String,
    val sell: String,
    val date: String
)
