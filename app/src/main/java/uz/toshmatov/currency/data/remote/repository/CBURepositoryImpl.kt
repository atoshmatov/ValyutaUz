package uz.toshmatov.currency.data.remote.repository

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.core.utils.Resource
import uz.toshmatov.currency.core.utils.errorData
import uz.toshmatov.currency.core.utils.loading
import uz.toshmatov.currency.core.utils.success
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.CurrencyDatabase
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
    private val cbuDao: CBUDao,
    private val database: CurrencyDatabase
) : CBURepository {

    companion object {
        private const val DATA_FRESHNESS_WINDOW_MILLIS = 6 * 60 * 60 * 1000L
    }

    override fun getCBUCurrencyList(): Flow<List<CBUModel>> {
        val lastUpdate = prefs.get(PrefKeys.CBU_DATE_KEY, 0L)
        return if (isLocalDataUpToDate(lastUpdate)) {
            getLocalCBUCurrencyList()
        } else {
            getRemoteCBUCurrencyList()
        }
    }

    override fun getCurrencyList(): Flow<Resource<List<CBUModel>>> =
        flow {
            emit(loading())
            val lastUpdate = getLastUpdateTimestamp()
            val response = if (isLocalDataUpToDate(lastUpdate)) {
                getLocalCBUCurrencyList()
            } else {
                getRemoteCBUCurrencyList()
            }
            response.collect {
                emit(success(it))
            }
        }.catch {
            emit(errorData(it.message))
        }

    override fun getLastUpdateTimestamp(): Long {
        return prefs.get(PrefKeys.CBU_DATE_KEY, 0L)
    }

    override fun isLocalDataStale(): Boolean {
        return !isLocalDataUpToDate(getLastUpdateTimestamp())
    }

    private fun isLocalDataUpToDate(lastUpdate: Long): Boolean {
        return System.currentTimeMillis() - lastUpdate < DATA_FRESHNESS_WINDOW_MILLIS
    }

    private fun getLocalCBUCurrencyList(): Flow<List<CBUModel>> {
        return cbuDao.getCBUDataList()
            .map { cbuEntityList ->
                cbuEntityList.map(cbuDaoMapper::mapFromEntity)
            }.catch {
                logError { "getLocalCBUCurrencyList: ${it.message}" }
            }.flowOn(Dispatchers.IO)
    }

    private fun getRemoteCBUCurrencyList(): Flow<List<CBUModel>> {
        return cbuApiService.getCBUCurrencyList()
            .onEach { cbuDtoList ->
                updateLocalData(cbuDtoList)
                prefs.save(PrefKeys.CBU_DATE_KEY, System.currentTimeMillis())
            }.map { cbuDtoList ->
                cbuDtoList.map(cbuMapper::mapFromEntity)
            }.catch {
                logError { "getRemoteCBUCurrencyList: ${it.message}" }
                emitAll(getLocalCBUCurrencyList())
            }.flowOn(Dispatchers.IO)
    }

    private suspend fun updateLocalData(cbuDtoList: List<CBUDto>) {
        database.withTransaction {
            cbuDao.deleteAll()
            cbuDao.upsert(cbuDtoList.map(cbuNetMapper::mapToEntity))
        }
    }
}
