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
import uz.toshmatov.currency.core.logger.logError
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import uz.toshmatov.currency.domain.repository.ExchangeBankRepository
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import javax.inject.Inject

@HiltViewModel
class BankDetailViewModel @Inject constructor(
    private val exchangeBankRepository: ExchangeBankRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val bankList = ArrayList<ExchangeBankModel>()

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

    fun getExchangeBankData() {
        exchangeBankRepository.exchangeBankData()
            .onStart {
                _state.update { detailState ->
                    detailState.copy(loading = true)
                }
            }.onEach { exchangeRateModel ->
                exchangeRateModel.apply {
                    bankList.clear()
                    bankList.addAll(this)
                    filter()
                }

                _state.update { homeState ->
                    homeState.copy(
                        loading = false,
                        listSize = exchangeRateModel.size
                    )
                }
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
                    exchangeRateList = bankList.toPersistentList()
                )
            }
            return
        }

        _state.update {
            it.copy(
                exchangeRateList = bankList.filter { cbu ->
                    cbu.bank.contains(query, ignoreCase = true)
                }.toPersistentList()
            )
        }
    }
}