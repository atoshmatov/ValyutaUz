package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.toshmatov.currency.data.mapper.ExchangeMapper
import uz.toshmatov.currency.data.remote.api.ExchangeRateDataSource
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import uz.toshmatov.currency.domain.repository.ExchangeRateRepository
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateApiService: ExchangeRateDataSource,
    private val exchangeMapper: ExchangeMapper
) : ExchangeRateRepository {
    override fun exchangeRateData(): Flow<List<ExchangeBankModel>> {
        return exchangeRateApiService.exchangeRateData().map {
            it.get("exchange_rates").asJsonArray.map { jsonElement ->
                exchangeMapper.mapFromEntity(jsonElement.asJsonObject)
            }
        }.flowOn(Dispatchers.IO)
    }
}