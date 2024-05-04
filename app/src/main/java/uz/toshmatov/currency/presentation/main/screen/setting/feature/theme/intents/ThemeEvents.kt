package uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.intents

import uz.toshmatov.currency.data.local.model.ThemeMode

sealed interface ThemeEvents {
    data class UpdateTheme(val themeMode: ThemeMode) : ThemeEvents
}