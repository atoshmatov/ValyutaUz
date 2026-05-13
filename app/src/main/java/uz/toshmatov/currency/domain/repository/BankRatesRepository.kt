package uz.toshmatov.currency.domain.repository

import uz.toshmatov.currency.domain.model.BankRateModel

interface BankRatesRepository {
    suspend fun getBankRates(currency: String): List<BankRateModel>
}
