package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.dao.NBUDao
import uz.toshmatov.currency.data.mapper.nbu.NBUDaoMapper
import uz.toshmatov.currency.data.mapper.nbu.NBUMapper
import uz.toshmatov.currency.data.mapper.nbu.NBUNetMapper
import uz.toshmatov.currency.data.remote.api.NBUApiService
import uz.toshmatov.currency.data.remote.model.NBUDto
import uz.toshmatov.currency.domain.model.NBUModel
import uz.toshmatov.currency.domain.repository.NBURepository
import javax.inject.Inject

class NBURepositoryImpl @Inject constructor(
    private val nbuApiService: NBUApiService,
    private val nbuMapper: NBUMapper,
    private val nbuNetMapper: NBUNetMapper,
    private val nbuDaoMapper: NBUDaoMapper,
    private val nbuDao: NBUDao,
    private val prefs: Prefs
) : NBURepository {
    override fun getNBUCurrencyList(): Flow<List<NBUModel>> {
        val lastUpdate = prefs.get(PrefKeys.nbuDateKey, 0L)

        val isLocalDataActual = isLocalDataUpToDate(lastUpdate)
        return if (isLocalDataActual) {
            getLocalNBUCurrencyList()
        } else {
            getRemoteNBUCurrencyList()
        }
    }

    private fun isLocalDataUpToDate(lastUpdate: Long): Boolean {
        return System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000
    }

    private fun getLocalNBUCurrencyList(): Flow<List<NBUModel>> {
        return nbuDao.getCBUData()
            .map { nbuEntityList ->
                nbuEntityList
                    .map(nbuDaoMapper::mapFromEntity)
                    .filter { nbu ->
                        nbu.nbuCellPrice.isNotEmpty() && nbu.nbuBuyPrice.isNotEmpty()
                    }
            }.catch { }
            .flowOn(Dispatchers.IO)
    }

    private fun getRemoteNBUCurrencyList(): Flow<List<NBUModel>> {
        return nbuApiService.getNBUCurrencyList()
            .onEach { cbuDtoList ->
                updateLocalData(cbuDtoList)
                prefs.save(PrefKeys.nbuDateKey, System.currentTimeMillis())
            }.map { cbuDtoList ->
                cbuDtoList.map(nbuMapper::mapFromEntity)
                    .filter { nbu ->
                        nbu.nbuCellPrice.isNotEmpty() && nbu.nbuBuyPrice.isNotEmpty()
                    }
            }.catch { }
            .flowOn(Dispatchers.IO)
    }

    private suspend fun updateLocalData(nbuDtoList: List<NBUDto>) {
        nbuDao.deleteAll()
        nbuDao.upsert(nbuDtoList.map(nbuNetMapper::mapToEntity))
    }
}