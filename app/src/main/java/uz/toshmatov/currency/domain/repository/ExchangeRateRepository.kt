package uz.toshmatov.currency.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.domain.model.ExchangeBankModel

interface ExchangeRateRepository {
    fun exchangeRateData(): Flow<List<ExchangeBankModel>>
}