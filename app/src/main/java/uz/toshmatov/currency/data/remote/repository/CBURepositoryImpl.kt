package uz.toshmatov.currency.data.remote.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.mapper.CBUMapper
import uz.toshmatov.currency.data.mapper.localmapper.CBUDaoMapper
import uz.toshmatov.currency.data.mapper.localmapper.CBUNetMapper
import uz.toshmatov.currency.data.remote.api.CBUApiService
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

        // 6 hour
        val isLocalDataActual = System.currentTimeMillis() - lastUpdate < 6 * 60 * 60 * 1000
        return if (isLocalDataActual) {
            cbuDao.getCBUDataList().map {
                it.map(cbuDaoMapper::mapFromEntity)
            }.flowOn(Dispatchers.IO)
        } else {
            cbuApiService.getCBUCurrencyList()
                .onEach {
                    cbuDao.deleteAll()
                    cbuDao.upsert(
                        it.map { model ->
                            cbuNetMapper.mapToEntity(model)
                        }
                    )
                    prefs.save(PrefKeys.cbuDateKey, System.currentTimeMillis())
                }.map {
                    it.map(cbuMapper::mapFromEntity)
                }.flowOn(Dispatchers.IO)
        }
    }
}