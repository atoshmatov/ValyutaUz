package uz.toshmatov.currency.presentation.main.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.core.logger.logInfo
import uz.toshmatov.currency.data.local.repository.DataStoreRepository
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.ExchangeRateRepository
import uz.toshmatov.currency.domain.repository.NBURepository
import uz.toshmatov.currency.presentation.main.screen.home.intents.HomeEvents
import uz.toshmatov.currency.presentation.main.screen.home.intents.HomeState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cbuRepository: CBURepository,
    private val nbuRepository: NBURepository,
    private val storeRepository: DataStoreRepository,
    private val exchangeRateRepository: ExchangeRateRepository
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getCBUCurrencyList()
        getNBUCurrencyList()
        getExchangeRateData()
        getCBUData()
    }

    fun reduce(event: HomeEvents) {}

    private fun getCBUCurrencyList() {
        cbuRepository.getCBUCurrencyList()
            .onStart {
                _state.update {
                    it.copy(isLoading = true)
                }
            }.onEach { cbuModel ->
                _state.update {
                    it.copy(
                        cbuList = cbuModel.toPersistentList(),
                        isLoading = false
                    )
                }
                cbuModel.forEach {
                    if (it.ccy == "USD")
                        setCbuData(it.rate)
                }
            }.catch {
                _state.update {
                    it.copy(
                        error = "Error",
                        isLoading = false
                    )
                }
                logError { it.localizedMessage ?: "" }
            }
            .launchIn(viewModelScope)
    }

    private fun setCbuData(cbuData: String) {
        viewModelScope.launch {
            storeRepository.setCBUData(cbuData)
        }
    }

    private fun getCBUData() {
        viewModelScope.launch {
            storeRepository.getCBUData()
                .onEach { cbu ->
                    _state.update {
                        it.copy(cbuData = cbu)
                    }
                }.launchIn(viewModelScope)
        }
    }

    private fun getNBUCurrencyList() {
        nbuRepository.getNBUCurrencyList()
            .onStart {
                _state.update {
                    it.copy(isLoading = true)
                }
            }.onEach { nbuModel ->
                _state.update {
                    it.copy(
                        nbuList = nbuModel.toPersistentList(),
                        isLoading = false
                    )
                }
            }.catch {
                _state.update {
                    it.copy(
                        error = "Error",
                        isLoading = false
                    )
                }
                logError { it.localizedMessage ?: "" }
            }
            .launchIn(viewModelScope)
    }


    private fun getExchangeRateData() {
        exchangeRateRepository.exchangeRateData()
            .onStart {
                _state.update {
                    it.copy(isLoading = true)
                }
            }.onEach { exchangeRateModel ->

                logInfo {
                    "getExchangeRateData --> $exchangeRateModel"
                }
                _state.update {
                    it.copy(
                        exchangeRateList = exchangeRateModel.toPersistentList(),
                        isLoading = false
                    )
                }
            }.catch {
                _state.update {
                    it.copy(
                        error = "Error",
                        isLoading = false
                    )
                }
                logError { it.localizedMessage ?: "" }
            }.launchIn(viewModelScope)
    }
}