package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.toshmatov.currency.data.mapper.CBUMapper
import uz.toshmatov.currency.data.remote.api.CBUApiService
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.repository.CBURepository
import javax.inject.Inject

class CBURepositoryImpl @Inject constructor(
    private val cbuApiService: CBUApiService,
    private val cbuMapper: CBUMapper,
) : CBURepository {
    override fun getCBUCurrencyList(): Flow<List<CBUModel>> {
        return cbuApiService.getCBUCurrencyList().map {
            it.map(cbuMapper::mapFromEntity)
        }.flowOn(Dispatchers.IO)
    }
}