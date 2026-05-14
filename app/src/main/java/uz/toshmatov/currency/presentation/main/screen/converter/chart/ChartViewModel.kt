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

enum class ChartPeriod(val days: Int, val label: String) {
    DAYS_7(7, "7 kun"),
    DAYS_14(14, "14 kun"),
    MONTH_1(30, "1 oy"),
    MONTH_3(90, "3 oy"),
    MONTH_6(180, "6 oy")
}

data class ChartState(
    val points: List<CurrencyChartPoint> = emptyList(),
    val isLoading: Boolean = false,
    val period: ChartPeriod = ChartPeriod.DAYS_7,
    val loadedCode: String = ""
)

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val cbuRepository: CBURepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state = _state.asStateFlow()

    private var loadJob: Job? = null

    fun load(currencyCode: String, period: ChartPeriod = ChartPeriod.DAYS_7) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    period = period,
                    points = emptyList(),
                    loadedCode = currencyCode
                )
            }
            try {
                val points = cbuRepository.getCurrencyHistory(currencyCode, period.days)
                _state.update { it.copy(points = points, isLoading = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}