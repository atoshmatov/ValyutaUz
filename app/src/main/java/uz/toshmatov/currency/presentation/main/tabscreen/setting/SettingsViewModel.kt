package uz.toshmatov.currency.presentation.main.tabscreen.setting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.tabscreen.setting.intents.SettingsState
import uz.toshmatov.currency.presentation.main.tabscreen.setting.model.ActionType
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<SettingsState> = MutableStateFlow(
        SettingsState()
    )
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val settings = listOf(
        uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel(
            string.settings_language,
            drawable.ic_language,
            ActionType.LANGUAGE
        ),
        uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel(
            string.settings_theme,
            drawable.ic_style,
            ActionType.THEME
        ),
        uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel(
            string.settings_info,
            drawable.ic_info_app,
            ActionType.ABOUT_APP
        ),
        uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel(
            string.settings_contact,
            drawable.ic_contact,
            ActionType.CONTACT_US,
            true
        ),
        uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel(
            string.settings_share,
            drawable.ic_share,
            ActionType.SHARE_APP,
            true
        ),
    )

    init {
        _state.update {
            it.copy(settings = settings.toPersistentList())
        }
    }
}