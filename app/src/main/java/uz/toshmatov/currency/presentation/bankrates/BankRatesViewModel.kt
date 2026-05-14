package uz.toshmatov.currency.presentation.bankrates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.toshmatov.currency.domain.model.BankRateModel
import uz.toshmatov.currency.domain.repository.BankRatesRepository
import javax.inject.Inject

data class BankRatesState(
    val rates: List<BankRateModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = "",
    val selectedCurrency: String = "USD"
)

@HiltViewModel
class BankRatesViewModel @Inject constructor(
    private val repository: BankRatesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BankRatesState())
    val state = _state.asStateFlow()

    init {
        load("USD")
    }

    fun load(currency: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, selectedCurrency = currency, error = "") }
            try {
                val rates = repository.getBankRates(currency)
                _state.update { it.copy(rates = rates, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message ?: "Xatolik") }
            }
        }
    }
}
