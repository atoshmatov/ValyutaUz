package uz.toshmatov.currency.data.local.themedatastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import uz.toshmatov.currency.data.local.model.ThemeMode
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "current_theme")
private val THEME_MODE_KEY = stringPreferencesKey("theme")
private val CBU_DATA_KEY = stringPreferencesKey("cbu_data")
private const val LIGHT = "Light"
private const val DARK = "Dark"
private const val SYSTEM = "System"

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

    suspend fun clear() {
        store.edit { preferences ->
            preferences.clear()
        }
    }
}