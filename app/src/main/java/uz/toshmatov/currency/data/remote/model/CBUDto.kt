package uz.toshmatov.currency.data.remote.model

import com.google.gson.annotations.SerializedName

data class CBUDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("Code")
    val code: String,
    @SerializedName("Ccy")
    val currencyCode: String,
    @SerializedName("CcyNm_RU")
    val currencyNameRU: String,
    @SerializedName("CcyNm_UZ")
    val currencyNameUZ: String,
    @SerializedName("CcyNm_UZC")
    val currencyNameUZC: String,
    @SerializedName("CcyNm_EN")
    val currencyNameEN: String,
    @SerializedName("Nominal")
    val nominal: String,
    @SerializedName("Rate")
    val rate: String,
    @SerializedName("Diff")
    val difference: String,
    @SerializedName("Date")
    val date: String
)