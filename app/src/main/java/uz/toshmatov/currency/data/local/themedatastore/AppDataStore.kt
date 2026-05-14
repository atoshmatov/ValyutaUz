package uz.toshmatov.currency.data.local.themedatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "current_theme")
private val THEME_MODE_KEY = stringPreferencesKey("theme")
private val ACCENT_COLOR_KEY = stringPreferencesKey("accent_color")
private val CBU_DATA_KEY = stringPreferencesKey("cbu_data")
private val SELECTED_WIDGET_CODES_KEY = stringPreferencesKey("selected_widget_codes")
private val DAILY_NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("daily_notification_enabled")
private val DAILY_NOTIFICATION_TIME_KEY = stringPreferencesKey("daily_notification_time")
private const val LIGHT = "Light"
private const val DARK = "Dark"
private const val SYSTEM = "System"
private val DEFAULT_WIDGET_CODES = setOf("USD", "EUR")
private const val DEFAULT_DAILY_NOTIFICATION_TIME = "09:00"
private const val CODES_SEPARATOR = ","

class AppDataStore @Inject constructor(context: Context) {
    private val store = context.dataStore

    fun getThemeMode(): Flow<ThemeMode> {
        return store.data
            .map { preferences ->
                when (preferences[THEME_MODE_KEY]) {
                    LIGHT -> ThemeMode.Light
                    DARK -> ThemeMode.Dark
                    SYSTEM -> ThemeMode.System
                    else -> ThemeMode.System
                }
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        val themeName = when (themeMode) {
            ThemeMode.Light -> LIGHT
            ThemeMode.Dark -> DARK
            ThemeMode.System -> SYSTEM
        }

        store.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeName
        }
    }

    fun getAccentColor(): Flow<AccentColor> {
        return store.data
            .map { preferences ->
                val name = preferences[ACCENT_COLOR_KEY] ?: AccentColor.Blue.name
                AccentColor.entries.firstOrNull { it.name == name } ?: AccentColor.Blue
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setAccentColor(accent: AccentColor) {
        store.edit { preferences ->
            preferences[ACCENT_COLOR_KEY] = accent.name
        }
    }

    fun getCBUData(): Flow<String> {
        return store.data
            .map { preferences ->
                preferences[CBU_DATA_KEY] ?: ""
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setCBUData(data: String) {
        store.edit { preferences ->
            preferences[CBU_DATA_KEY] = data
        }
    }

    fun getSelectedWidgetCodes(): Flow<Set<String>> {
        return store.data
            .map { preferences ->
                val rawCodes = preferences[SELECTED_WIDGET_CODES_KEY]
                if (rawCodes == null) {
                    DEFAULT_WIDGET_CODES
                } else {
                    rawCodes.split(CODES_SEPARATOR)
                        .map { it.trim().uppercase() }
                        .filter { it.isNotBlank() }
                        .toSet()
                }
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setSelectedWidgetCodes(codes: Set<String>) {
        store.edit { preferences ->
            preferences[SELECTED_WIDGET_CODES_KEY] = codes
                .map { it.trim().uppercase() }
                .filter { it.isNotBlank() }
                .joinToString(CODES_SEPARATOR)
        }
    }

    fun getDailyNotificationEnabled(): Flow<Boolean> {
        return store.data
            .map { preferences ->
                preferences[DAILY_NOTIFICATION_ENABLED_KEY] ?: true
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setDailyNotificationEnabled(enabled: Boolean) {
        store.edit { preferences ->
            preferences[DAILY_NOTIFICATION_ENABLED_KEY] = enabled
        }
    }

    fun getDailyNotificationTime(): Flow<String> {
        return store.data
            .map { preferences ->
                preferences[DAILY_NOTIFICATION_TIME_KEY] ?: DEFAULT_DAILY_NOTIFICATION_TIME
            }.flowOn(Dispatchers.IO)
    }

    suspend fun setDailyNotificationTime(time: String) {
        store.edit { preferences ->
            preferences[DAILY_NOTIFICATION_TIME_KEY] = time
        }
    }

    suspend fun clear() {
        store.edit { preferences ->
            preferences.clear()
        }
    }
}
