package uz.toshmatov.currency.presentation.main.tabscreen.setting.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel

data class SettingsState(
    val isLoading: Boolean = false,
    val settings: ImmutableList<uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel> = persistentListOf()
)