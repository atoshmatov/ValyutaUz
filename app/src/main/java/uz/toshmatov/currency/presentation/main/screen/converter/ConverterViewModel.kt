package uz.toshmatov.currency.presentation.main.screen.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.repository.CBURepository
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val cbuRepository: CBURepository
) : ViewModel() {

    private val _currencies = MutableStateFlow<List<CBUModel>>(emptyList())
    val currencies: StateFlow<List<CBUModel>> = _currencies.asStateFlow()

    init {
        observeCurrencies()
    }

    private fun observeCurrencies() {
        cbuRepository.getCBUCurrencyList()
            .onEach { cbuList ->
                _currencies.value = cbuList
                    .distinctBy { it.ccy.uppercase() }
                    .sortedBy { it.ccy.uppercase() }
            }
            .catch { throwable ->
                logError { "ConverterViewModel.observeCurrencies: ${throwable.localizedMessage}" }
                _currencies.value = emptyList()
            }
            .launchIn(viewModelScope)
    }
}
