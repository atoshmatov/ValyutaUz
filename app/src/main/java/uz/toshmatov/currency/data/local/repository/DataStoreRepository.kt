package uz.toshmatov.currency.data.local.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.model.ThemeMode

interface DataStoreRepository {

    fun getThemeMode():Flow<ThemeMode>

    suspend fun setThemeMode(themeMode: ThemeMode)

    fun getCBUData(): Flow<String>

    suspend fun setCBUData(data: String)
}