package uz.toshmatov.currency.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.data.local.datastore.ThemeDataStore
import uz.toshmatov.currency.data.local.model.ThemeMode
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeDataStore: ThemeDataStore
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(ThemeMode.System)
    val isDarkMode = _isDarkMode.asStateFlow()

    init {
        getThemeMode()
    }

    private fun getThemeMode() {
        themeDataStore.getThemeMode()
            .onEach { themeMode ->
                _isDarkMode.value = themeMode
            }.launchIn(viewModelScope)
    }
}