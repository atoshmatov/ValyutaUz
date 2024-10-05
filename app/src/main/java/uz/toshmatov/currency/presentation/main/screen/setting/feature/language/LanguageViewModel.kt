package uz.toshmatov.currency.presentation.main.screen.setting.feature.language

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.component.LangType
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.component.LanguageModel
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.intents.LanguageState
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<LanguageState> = MutableStateFlow(LanguageState())
    val state: StateFlow<LanguageState> = _state.asStateFlow()

    private val languageList = listOf(
        LanguageModel(string.uzbek_language, drawable.uzbekista_n, LangType.UZBEK, "uz"),
        LanguageModel(string.cyrillic_language, drawable.uzbekista_n, LangType.CYRILLIC, "csr"),
        LanguageModel(string.english_language, drawable.united_states, LangType.ENGLISH, "en"),
        LanguageModel(string.russian_language, drawable.russia, LangType.RUSSIAN, "ru"),
    )

    init {
        _state.update {
            it.copy(languageList = languageList.toPersistentList())
        }
    }
}