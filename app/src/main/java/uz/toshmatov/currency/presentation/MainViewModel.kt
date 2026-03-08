package uz.toshmatov.currency.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dateStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _isDarkMode = MutableStateFlow(
        runBlocking {
            dateStoreRepository.getThemeMode().first()
        }
    )
    val isDarkMode = _isDarkMode.asStateFlow()

    private val _isOnline = MutableStateFlow(false)
    val isOnline = _isOnline.asStateFlow()

    init {
        getThemeMode()
    }

    private fun getThemeMode() {
        dateStoreRepository.getThemeMode()
            .onEach { themeMode ->
                _isDarkMode.value = themeMode
            }.launchIn(viewModelScope)
    }
}
