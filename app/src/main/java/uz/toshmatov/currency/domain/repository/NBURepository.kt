package uz.toshmatov.currency.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.remote.model.NBUDto
import uz.toshmatov.currency.domain.model.NBUModel

interface NBURepository {

    fun getNBUCurrencyList(): Flow<List<NBUModel>>
}