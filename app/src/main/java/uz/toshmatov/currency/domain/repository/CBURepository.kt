package uz.toshmatov.currency.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.domain.model.CBUModel

interface CBURepository {

    fun getCBUCurrencyList(): Flow<List<CBUModel>>
}