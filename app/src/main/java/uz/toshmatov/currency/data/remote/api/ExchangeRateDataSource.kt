package uz.toshmatov.currency.data.remote.api

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface ExchangeRateDataSource {

    fun exchangeRateData(): Flow<JsonObject>
}