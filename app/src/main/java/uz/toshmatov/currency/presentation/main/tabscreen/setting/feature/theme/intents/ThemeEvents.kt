package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents

import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode

sealed interface ThemeEvents {
    data class UpdateTheme(val themeMode: ThemeMode) : ThemeEvents
    data class UpdateAccentColor(val accent: AccentColor) : ThemeEvents
}
