package uz.toshmatov.currency.data.local.repository

import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.data.local.themedatastore.AppDataStore
import uz.toshmatov.currency.domain.repository.DataStoreRepository
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

    override fun getAccentColor(): Flow<AccentColor> = dataStore.getAccentColor()

    override suspend fun setAccentColor(accent: AccentColor) = dataStore.setAccentColor(accent)

    override fun getCBUData(): Flow<String> {
        return dataStore.getCBUData()
    }

    override suspend fun setCBUData(data: String) {
        dataStore.setCBUData(data)
    }

    override fun getSelectedWidgetCodes(): Flow<Set<String>> {
        return dataStore.getSelectedWidgetCodes()
    }

    override suspend fun setSelectedWidgetCodes(codes: Set<String>) {
        dataStore.setSelectedWidgetCodes(codes)
    }

    override fun getDailyNotificationEnabled(): Flow<Boolean> {
        return dataStore.getDailyNotificationEnabled()
    }

    override suspend fun setDailyNotificationEnabled(enabled: Boolean) {
        dataStore.setDailyNotificationEnabled(enabled)
    }

    override fun getDailyNotificationTime(): Flow<String> {
        return dataStore.getDailyNotificationTime()
    }

    override suspend fun setDailyNotificationTime(time: String) {
        dataStore.setDailyNotificationTime(time)
    }
}
