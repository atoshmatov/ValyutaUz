package uz.toshmatov.currency.data.local.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.data.local.themedatastore.AppDataStore
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: AppDataStore
) : DataStoreRepository {
    override fun getThemeMode(): Flow<ThemeMode> {
        return dataStore.getThemeMode()
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.setThemeMode(themeMode)
    }

    override fun getCBUData(): Flow<String> {
        return dataStore.getCBUData()
    }

    override suspend fun setCBUData(data: String) {
        dataStore.setCBUData(data)
    }
}