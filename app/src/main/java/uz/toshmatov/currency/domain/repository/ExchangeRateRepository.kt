package uz.toshmatov.currency.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.remote.model.ExchangeDto
import uz.toshmatov.currency.domain.model.ExchangeModel

interface ExchangeRateRepository {
    fun exchangeRateData(): Flow<List<ExchangeModel>>
}