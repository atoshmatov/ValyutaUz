package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents

import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode

data class ThemeState(
    val currentThemeMode: ThemeMode = ThemeMode.System,
    val currentAccentColor: AccentColor = AccentColor.Blue,
)
