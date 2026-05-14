package uz.toshmatov.currency.data.remote.repository

import androidx.room.withTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.core.utils.Resource
import uz.toshmatov.currency.core.utils.errorData
import uz.toshmatov.currency.core.utils.loading
import uz.toshmatov.currency.core.utils.success
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.CurrencyDatabase
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.local.room.dao.CurrencyHistoryDao
import uz.toshmatov.currency.data.local.room.entity.CurrencyHistoryEntity
import uz.toshmatov.currency.data.mapper.cbu.CBUDaoMapper
import uz.toshmatov.currency.data.mapper.cbu.CBUMapper
import uz.toshmatov.currency.data.mapper.cbu.CBUNetMapper
import uz.toshmatov.currency.data.remote.api.CBUApiService
import uz.toshmatov.currency.data.remote.model.CBUDto
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.model.CurrencyChartPoint
import uz.toshmatov.currency.domain.repository.CBURepository
import java.util.Calendar
import javax.inject.Inject

class CBURepositoryImpl @Inject constructor(
    private val cbuApiService: CBUApiService,
    private val cbuMapper: CBUMapper,
    private val cbuNetMapper: CBUNetMapper,
    private val cbuDaoMapper: CBUDaoMapper,
    private val prefs: Prefs,
    private val cbuDao: CBUDao,
    private val historyDao: CurrencyHistoryDao,
    private val database: CurrencyDatabase
) : CBURepository {

    companion object {
        private const val DATA_FRESHNESS_WINDOW_MILLIS = 6 * 60 * 60 * 1000L
    }

    override fun getCBUCurrencyList(): Flow<List<CBUModel>> {
        val lastUpdate = prefs.get(PrefKeys.CBU_DATE_KEY, 0L)
        return if (isLocalDataUpToDate(lastUpdate)) getLocalCBUCurrencyList()
        else getRemoteCBUCurrencyList()
    }

    override fun getCurrencyList(): Flow<Resource<List<CBUModel>>> = flow {
        emit(loading())
        val response = if (isLocalDataUpToDate(getLastUpdateTimestamp())) getLocalCBUCurrencyList()
        else getRemoteCBUCurrencyList()
        response.collect { emit(success(it)) }
    }.catch { emit(errorData(it.message)) }

    override fun getLastUpdateTimestamp(): Long = prefs.get(PrefKeys.CBU_DATE_KEY, 0L)

    override fun isLocalDataStale(): Boolean = !isLocalDataUpToDate(getLastUpdateTimestamp())

    private fun isLocalDataUpToDate(lastUpdate: Long) =
        System.currentTimeMillis() - lastUpdate < DATA_FRESHNESS_WINDOW_MILLIS

    private fun getLocalCBUCurrencyList(): Flow<List<CBUModel>> =
        cbuDao.getCBUDataList()
            .map { it.map(cbuDaoMapper::mapFromEntity) }
            .catch { logError { "getLocalCBUCurrencyList: ${it.message}" } }
            .flowOn(Dispatchers.IO)

    private fun getRemoteCBUCurrencyList(): Flow<List<CBUModel>> =
        cbuApiService.getCBUCurrencyList()
            .onEach { list ->
                updateLocalData(list)
                prefs.save(PrefKeys.CBU_DATE_KEY, System.currentTimeMillis())
            }
            .map { it.map(cbuMapper::mapFromEntity) }
            .catch {
                logError { "getRemoteCBUCurrencyList: ${it.message}" }
                emitAll(getLocalCBUCurrencyList())
            }
            .flowOn(Dispatchers.IO)

    override suspend fun getCurrencyHistory(code: String, days: Int): List<CurrencyChartPoint> =
        withContext(Dispatchers.IO) {
            val calendar = Calendar.getInstance()

            // Build ordered list: (display: DD.MM.YYYY, api: YYYY-MM-DD, sortKey: YYYYMMDD)
            data class DateEntry(val display: String, val api: String, val sortKey: Int)

            val dates = (0 until days).map {
                val day = "%02d".format(calendar.get(Calendar.DAY_OF_MONTH))
                val month = "%02d".format(calendar.get(Calendar.MONTH) + 1)
                val year = calendar.get(Calendar.YEAR)
                calendar.add(Calendar.DAY_OF_MONTH, -1)
                DateEntry(
                    display = "$day.$month.$year",
                    api = "$year-$month-$day",
                    sortKey = "$year$month$day".toInt()
                )
            }.reversed() // oldest → newest

            val targetDisplayDates = dates.map { it.display }.toSet()
            val cachedDates = historyDao.getCachedDates(code).toSet()
            val missing = dates.filter { it.display !in cachedDates }

            // Fetch missing dates in parallel (max 10 concurrent)
            if (missing.isNotEmpty()) {
                val newEntities = coroutineScope {
                    missing.chunked(10).flatMap { chunk ->
                        chunk.map { entry ->
                            async {
                                try {
                                    val result = cbuApiService.getCurrencyByDate(code, entry.api)
                                    result.firstOrNull()?.let { dto ->
                                        val rate = dto.rate.toFloatOrNull() ?: return@let null
                                        if (rate > 0f) CurrencyHistoryEntity(code, entry.display, rate)
                                        else null
                                    }
                                } catch (e: Exception) { null }
                            }
                        }.awaitAll().filterNotNull()
                    }
                }
                if (newEntities.isNotEmpty()) historyDao.insertAll(newEntities)
            }

            // Fetch only requested dates and sort correctly (DD.MM.YYYY → YYYYMMDD)
            historyDao.getHistory(code)
                .filter { it.date in targetDisplayDates }
                .sortedBy { entity ->
                    val p = entity.date.split(".")
                    if (p.size == 3) "${p[2]}${p[1]}${p[0]}".toIntOrNull() ?: 0 else 0
                }
                .filter { it.rate > 0f }
                .map { CurrencyChartPoint(it.date, it.rate) }
        }

    private suspend fun updateLocalData(cbuDtoList: List<CBUDto>) {
        database.withTransaction {
            cbuDao.deleteAll()
            cbuDao.upsert(cbuDtoList.map(cbuNetMapper::mapToEntity))
        }
    }
}
