package uz.toshmatov.currency.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode

interface DataStoreRepository {

    fun getThemeMode(): Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)

    fun getAccentColor(): Flow<AccentColor>
    suspend fun setAccentColor(accent: AccentColor)

    fun getCBUData(): Flow<String>

    suspend fun setCBUData(data: String)

    fun getSelectedWidgetCodes(): Flow<Set<String>>

    suspend fun setSelectedWidgetCodes(codes: Set<String>)

    fun getDailyNotificationEnabled(): Flow<Boolean>

    suspend fun setDailyNotificationEnabled(enabled: Boolean)

    fun getDailyNotificationTime(): Flow<String>

    suspend fun setDailyNotificationTime(time: String)
}
