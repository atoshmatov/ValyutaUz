package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.mapper.cbu.CBUDaoMapper
import uz.toshmatov.currency.data.mapper.cbu.CBUMapper
import uz.toshmatov.currency.data.mapper.cbu.CBUNetMapper
import uz.toshmatov.currency.data.remote.api.CBUApiService
import uz.toshmatov.currency.data.remote.model.CBUDto
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.repository.CBURepository
import javax.inject.Inject

class CBURepositoryImpl @Inject constructor(
    private val cbuApiService: CBUApiService,
    private val cbuMapper: CBUMapper,
    private val cbuNetMapper: CBUNetMapper,
    private val cbuDaoMapper: CBUDaoMapper,
    private val prefs: Prefs,
    private val cbuDao: CBUDao
) : CBURepository {
    override fun getCBUCurrencyList(): Flow<List<CBUModel>> {
        val lastUpdate = prefs.get(PrefKeys.cbuDateKey, 0L)

        val isLocalDataActual = isLocalDataUpToDate(lastUpdate)
        return if (isLocalDataActual) {
            getLocalCBUCurrencyList()
        } else {
            getRemoteCBUCurrencyList()
        }
    }

    private fun isLocalDataUpToDate(lastUpdate: Long): Boolean {
        return System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000
    }

    private fun getLocalCBUCurrencyList(): Flow<List<CBUModel>> {
        return cbuDao.getCBUDataList()
            .map { cbuEntityList ->
                cbuEntityList.map(cbuDaoMapper::mapFromEntity)
            }.catch { }
            .flowOn(Dispatchers.IO)
    }

    private fun getRemoteCBUCurrencyList(): Flow<List<CBUModel>> {
        return cbuApiService.getCBUCurrencyList()
            .onEach { cbuDtoList ->
                updateLocalData(cbuDtoList)
                prefs.save(PrefKeys.cbuDateKey, cbuDtoList.first().date)
            }.map { cbuDtoList ->
                cbuDtoList.map(cbuMapper::mapFromEntity)
            }.catch { }
            .flowOn(Dispatchers.IO)
    }

    private suspend fun updateLocalData(cbuDtoList: List<CBUDto>) {
        cbuDao.deleteAll()
        cbuDao.upsert(cbuDtoList.map(cbuNetMapper::mapToEntity))
    }
}