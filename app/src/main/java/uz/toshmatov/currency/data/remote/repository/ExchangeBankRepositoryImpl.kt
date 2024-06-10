package uz.toshmatov.currency.data.remote.repository

import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.local.room.dao.BanksDao
import uz.toshmatov.currency.data.mapper.bank.BankDaoMapper
import uz.toshmatov.currency.data.mapper.bank.BankMapper
import uz.toshmatov.currency.data.mapper.bank.BankNetMapper
import uz.toshmatov.currency.data.remote.api.ExchangeBankDataSource
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import uz.toshmatov.currency.domain.repository.ExchangeBankRepository
import javax.inject.Inject

class ExchangeBankRepositoryImpl @Inject constructor(
    private val exchangeBankDataSource: ExchangeBankDataSource,
    private val bankMapper: BankMapper,
    private val bankNetMapper: BankNetMapper,
    private val bankDaoMapper: BankDaoMapper,
    private val bankDao: BanksDao,
    private val prefs: Prefs,
) : ExchangeBankRepository {
    override fun exchangeBankData(): Flow<List<ExchangeBankModel>> {
        val lastUpdate = prefs.get(PrefKeys.bankDateKey, 0L)

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

    private fun getLocalCBUCurrencyList(): Flow<List<ExchangeBankModel>> {
        return bankDao.getAll()
            .map { bankEntityList ->
                bankEntityList.map(bankDaoMapper::mapFromEntity)
            }.flowOn(Dispatchers.IO)
    }

    private fun getRemoteCBUCurrencyList(): Flow<List<ExchangeBankModel>> {
        return exchangeBankDataSource.exchangeBankData()
            .onEach {
                updateLocalData(it)
                prefs.save(PrefKeys.bankDateKey, System.currentTimeMillis())
            }.map { bankJson ->
                bankJson.get("exchange_rates").asJsonArray.map { jsonElement ->
                    bankMapper.mapFromEntity(jsonElement.asJsonObject)
                }
            }.flowOn(Dispatchers.IO)
    }

    private suspend fun updateLocalData(cbuDtoList: JsonObject) {
        bankDao.deleteAll()
        bankDao.upsert(cbuDtoList.get("exchange_rates").asJsonArray.map { jsonElement ->
            bankNetMapper.mapToEntity(jsonElement.asJsonObject)
        })
    }
}