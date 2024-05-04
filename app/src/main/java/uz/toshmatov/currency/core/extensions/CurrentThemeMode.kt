package uz.toshmatov.currency.core.extensions

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import uz.toshmatov.currency.data.local.model.ThemeMode

@Composable
fun getCurrentThemeMode(themeMode: ThemeMode): Boolean {
    return when (themeMode) {
        ThemeMode.Dark -> true
        ThemeMode.Light -> false
        ThemeMode.System -> isSystemInDarkTheme()
    }
}