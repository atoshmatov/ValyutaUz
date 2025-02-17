package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.language.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.language.component.LanguageModel

data class LanguageState(
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val languageList: ImmutableList<LanguageModel> = persistentListOf()
)
