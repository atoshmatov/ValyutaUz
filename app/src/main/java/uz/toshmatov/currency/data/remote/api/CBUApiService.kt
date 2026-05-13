package uz.toshmatov.currency.data.remote.api

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query
import uz.toshmatov.currency.data.remote.model.CBUDto

interface CBUApiService {

    @GET("arkhiv-kursov-valyut/json/")
    fun getCBUCurrencyList(): Flow<List<CBUDto>>

    @GET("arkhiv-kursov-valyut/json/")
    suspend fun getCBUByDate(@Query("date") date: String): List<CBUDto>
}