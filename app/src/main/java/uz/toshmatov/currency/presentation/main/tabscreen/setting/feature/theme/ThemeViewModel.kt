package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents.ThemeEvents
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents.ThemeState
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val storeRepository: DataStoreRepository
) : ViewModel() {

    private val _state: MutableStateFlow<ThemeState> = MutableStateFlow(ThemeState())
    val state: StateFlow<ThemeState> = _state.asStateFlow()

    init {
        storeRepository.getThemeMode()
            .onEach { mode -> _state.update { it.copy(currentThemeMode = mode) } }
            .launchIn(viewModelScope)

        storeRepository.getAccentColor()
            .onEach { accent -> _state.update { it.copy(currentAccentColor = accent) } }
            .launchIn(viewModelScope)
    }

    fun reduce(event: ThemeEvents) {
        when (event) {
            is ThemeEvents.UpdateTheme -> viewModelScope.launch {
                storeRepository.setThemeMode(event.themeMode)
            }
            is ThemeEvents.UpdateAccentColor -> viewModelScope.launch {
                storeRepository.setAccentColor(event.accent)
            }
        }
    }
}