package uz.toshmatov.currency.data.remote.model

import com.google.gson.annotations.SerializedName

data class ExchangeDto (
    @SerializedName("bank") val bank: String,
    @SerializedName("buy") val buy: String,
    @SerializedName("sell") val sell: String,
    @SerializedName("date") val date: String
)