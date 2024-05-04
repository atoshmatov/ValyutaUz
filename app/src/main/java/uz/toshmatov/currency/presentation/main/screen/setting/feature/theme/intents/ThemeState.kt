package uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.component.ThemeModel

data class ThemeState(
    val isLoading: Boolean = false,
    val themeList: ImmutableList<ThemeModel> = persistentListOf(),
    val currentThemeMode: ThemeMode = ThemeMode.System,
)
