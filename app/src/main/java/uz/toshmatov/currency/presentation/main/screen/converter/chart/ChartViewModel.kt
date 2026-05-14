package uz.toshmatov.currency.presentation.main.screen.converter.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.toshmatov.currency.domain.model.CurrencyChartPoint
import uz.toshmatov.currency.domain.repository.CBURepository
import javax.inject.Inject

data class ChartState(
    val points: List<CurrencyChartPoint> = emptyList(),
    val isLoading: Boolean = false,
    val selectedDays: Int = 30,
    val loadedCode: String = ""
)

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val cbuRepository: CBURepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state = _state.asStateFlow()

    private var loadJob: Job? = null

    fun load(currencyCode: String, days: Int = 30) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, selectedDays = days, points = emptyList(), loadedCode = currencyCode) }
            try {
                val points = cbuRepository.getCurrencyHistory(currencyCode, days)
                _state.update { it.copy(points = points, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
