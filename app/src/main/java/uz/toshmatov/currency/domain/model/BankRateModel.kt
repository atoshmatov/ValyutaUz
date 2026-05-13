package uz.toshmatov.currency.domain.model

data class BankRateModel(
    val bankSlug: String,
    val bankName: String,
    val buy: Int,
    val sell: Int,
    val currency: String
)
