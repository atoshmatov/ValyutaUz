package uz.toshmatov.currency.presentation.main.screen.detail.viewModel

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
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import javax.inject.Inject

@HiltViewModel
class CBUDetailViewModel @Inject constructor(
    private val cbuRepository: CBURepository,
    private val storeRepository: DataStoreRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val cbuList = ArrayList<CBUModel>()

    init {
        getCBUCurrencyList()
        getCBUData()
    }

    fun reduce(event: DetailEvents) {
        when (event) {
            is DetailEvents.SearchQuery -> {
                _state.update {
                    it.copy(searchQuery = event.query)
                }
                filter()
            }
        }
    }

    private fun getCBUCurrencyList() {
        cbuRepository.getCurrencyList()
            .onStart {
                _state.update { detailState ->
                    detailState.copy(loading = true)
                }
            }.onEach { cbuModel ->
                _state.update { homeState ->
                    homeState.copy(loading = false)
                }

                cbuModel.apply {
                    cbuList.clear()
                    cbuList.addAll(this.data ?: emptyList())
                    filter()
                }

                cbuModel.data?.forEach {
                    if (it.ccy == "USD") setCbuData(it.rate)
                } ?: emptyList<CBUModel>()

            }.catch {
                _state.update { detailState ->
                    detailState.copy(
                        error = "Error",
                        loading = false
                    )
                }
                logError { it.localizedMessage ?: "" }
            }.launchIn(viewModelScope)
    }

    private fun filter() {
        val query = _state.value.searchQuery
        if (query.isEmpty()) {
            _state.update {
                it.copy(
                    cbuList = cbuList.toPersistentList()
                )
            }
            return
        }

        _state.update {
            it.copy(
                cbuList = cbuList.filter { cbu ->
                    cbu.ccy.contains(
                        query, ignoreCase = true
                    ) || cbu.ccyName.contains(
                        query,
                        ignoreCase = true
                    )
                }.toPersistentList()
            )
        }
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
                    _state.update { homeState ->
                        homeState.copy(cbuData = cbu)
                    }
                }.launchIn(viewModelScope)
        }
    }
}