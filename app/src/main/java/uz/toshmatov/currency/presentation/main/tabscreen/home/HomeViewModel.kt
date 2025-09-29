package uz.toshmatov.currency.presentation.main.tabscreen.home

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
import uz.toshmatov.currency.core.connect.ConnectivityObserver
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import uz.toshmatov.currency.presentation.main.tabscreen.home.intents.HomeState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val cbuRepository: CBURepository,
    private val storeRepository: DataStoreRepository,
    //private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getCBUCurrencyList()
        getCBUData()
        //getNetworkStatus()
    }

    private fun getCBUCurrencyList() {
        cbuRepository.getCBUCurrencyList()
            .onStart {
                _state.update { homeState ->
                    homeState.copy(isLoading = true)
                }
            }.catch {
                _state.update { homeState ->
                    homeState.copy(
                        error = "Error",
                        isLoading = false
                    )
                }
                logError { it.localizedMessage ?: "" }
            }.onEach { cbuModel ->
                _state.update { homeState ->
                    homeState.copy(
                        cbuList = cbuModel.take(20).toPersistentList(),
                        isLoading = false,
                        isEmptyCbuList = cbuModel.isEmpty()
                    )
                }
                cbuModel.forEach {
                    if (it.ccy == "USD")
                        setCbuData(it.rate)
                }
            }.launchIn(viewModelScope)

    }

    private fun setCbuData(cbuData: String) {
        viewModelScope.launch {
            storeRepository.setCBUData(cbuData)
        }
    }

    private fun getCBUData() {
        storeRepository.getCBUData()
            .onEach { cbu ->
                _state.update { homeState ->
                    homeState.copy(cbuData = cbu)
                }
            }.launchIn(viewModelScope)
    }

    /*private fun getNetworkStatus() {
        connectivityObserver.observe()
            .onEach { status ->
                _state.update { homeState ->
                    homeState.copy(networkStatus = status)
                }
            }.launchIn(viewModelScope)
    }*/
}