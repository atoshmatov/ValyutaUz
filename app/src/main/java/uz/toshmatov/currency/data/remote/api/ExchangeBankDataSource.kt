package uz.toshmatov.currency.data.remote.api

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

interface ExchangeBankDataSource {

    fun exchangeBankData(): Flow<JsonObject>
}