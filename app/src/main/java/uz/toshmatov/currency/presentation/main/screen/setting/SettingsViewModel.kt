package uz.toshmatov.currency.presentation.main.screen.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.setting.intents.SettingsEvents
import uz.toshmatov.currency.presentation.main.screen.setting.intents.SettingsState
import uz.toshmatov.currency.presentation.main.screen.setting.model.ActionType
import uz.toshmatov.currency.presentation.main.screen.setting.model.SettingModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val settings = listOf(
        SettingModel(string.settings_language, drawable.ic_language, ActionType.LANGUAGE),
        SettingModel(string.settings_theme, drawable.ic_style, ActionType.THEME),
        SettingModel(string.settings_info, drawable.ic_info_app, ActionType.ABOUT_APP),
        SettingModel(string.settings_contact, drawable.ic_contact, ActionType.CONTACT_US, true),
        SettingModel(string.settings_rate, drawable.ic_star, ActionType.RATE_APP, true),
        SettingModel(string.settings_share, drawable.ic_share, ActionType.SHARE_APP, true),
    )

    init {
        _state.update {
            it.copy(settings = settings.toPersistentList())
        }
    }

    fun reduce(event: SettingsEvents) {}
}