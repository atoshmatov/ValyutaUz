package uz.toshmatov.currency.data.remote.model

import com.google.gson.annotations.SerializedName

data class NBUDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("code")
    val code: String,
    @SerializedName("cb_price")
    val cbPrice: String,
    @SerializedName("nbu_buy_price")
    val nbuBuyPrice: String,
    @SerializedName("nbu_cell_price")
    val nbuCellPrice: String,
    @SerializedName("date")
    val date: String
)
