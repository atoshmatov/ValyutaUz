package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.domain.model.CBUModel

data class DailyUpdatesState(
    val isLoading: Boolean = false,
    val error: String = "",
    val selectedCodes: Set<String> = emptySet(),
    val notificationEnabled: Boolean = true,
    val notificationTime: String = "09:00",
    val currencyList: ImmutableList<CBUModel> = persistentListOf()
)
