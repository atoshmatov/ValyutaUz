package uz.toshmatov.currency.data.local.datastore

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

class ThemeDataStore @Inject constructor(context: Context) {
    private val store = context.dataStore

    fun getThemeMode(): Flow<ThemeMode> {
        return store.data.map { preferences ->
            when (preferences[THEME_MODE_KEY]) {
                "Light" -> ThemeMode.Light
                "Dark" -> ThemeMode.Dark
                "System" -> ThemeMode.System
                else -> ThemeMode.System
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        val themeName = when (themeMode) {
            ThemeMode.Light -> "Light"
            ThemeMode.Dark -> "Dark"
            ThemeMode.System -> "System"
        }

        store.edit { preferences ->
            preferences[THEME_MODE_KEY] = themeName
        }
    }

    suspend fun clear() {
        store.edit { preferences ->
            preferences.clear()
        }
    }
}