package uz.toshmatov.currency.data.remote.api

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import uz.toshmatov.currency.data.remote.model.CBUDto

interface CBUApiService {

    @GET("arkhiv-kursov-valyut/json/")
    fun getCBUCurrencyList(): Flow<List<CBUDto>>
}