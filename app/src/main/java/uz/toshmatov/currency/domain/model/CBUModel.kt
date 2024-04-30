package uz.toshmatov.currency.domain.model

import androidx.compose.runtime.Stable

@Stable
data class CBUModel(
    val id: Int,
    val code: String,
    val ccy: String,
    val ccyNmRU: String,
    val ccyNmUZ: String,
    val ccyNmUZC: String,
    val ccyNmEN: String,
    val nominal: String,
    val rate: String,
    val diff: String,
    val date: String
)