package uz.toshmatov.currency.data.remote.api

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.toshmatov.currency.data.remote.model.NBUDto

interface NBUApiService {

    @GET("exchange-rates/json/")
    fun getNBUCurrencyList(): Flow<List<NBUDto>>
}