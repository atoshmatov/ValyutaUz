package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.toshmatov.currency.data.mapper.NBUMapper
import uz.toshmatov.currency.data.remote.api.NBUApiService
import uz.toshmatov.currency.data.remote.model.NBUDto
import uz.toshmatov.currency.domain.model.NBUModel
import uz.toshmatov.currency.domain.repository.NBURepository
import javax.inject.Inject

class NBURepositoryImpl @Inject constructor(
    private val nbuApiService: NBUApiService,
    private val nbuMapper: NBUMapper
) : NBURepository {
    override fun getNBUCurrencyList(): Flow<List<NBUModel>> {
        return nbuApiService.getNBUCurrencyList().map {
            it.map(nbuMapper::mapFromEntity)
        }.flowOn(Dispatchers.IO)
    }
}