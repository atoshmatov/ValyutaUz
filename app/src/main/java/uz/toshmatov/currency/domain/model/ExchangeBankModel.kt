package uz.toshmatov.currency.domain.model

import androidx.compose.runtime.Stable

@Stable
data class ExchangeBankModel(
    val bank: String,
    val buy: String,
    val sell: String,
    val date: String
)
