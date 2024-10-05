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
import uz.toshmatov.currency.domain.model.NBUModel
import uz.toshmatov.currency.domain.repository.NBURepository
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import javax.inject.Inject

@HiltViewModel
class NBUDetailViewModel @Inject constructor(
    private val nbuRepository: NBURepository,
) : ViewModel() {

    private val _state: MutableStateFlow<DetailState> = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val nbuList = ArrayList<NBUModel>()

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

    fun getNBUCurrencyList() {
        viewModelScope.launch {
            nbuRepository.getNBUCurrencyList()
                .onStart {
                    _state.update { detailState ->
                        detailState.copy(loading = true)
                    }
                }.onEach { cbuModel ->
                    cbuModel.apply {
                        nbuList.clear()
                        nbuList.addAll(this)
                        filter()
                    }

                    _state.update { homeState ->
                        homeState.copy(
                            loading = false,
                            listSize = cbuModel.size
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
                }
                .launchIn(viewModelScope)
        }
    }

    private fun filter() {
        val query = _state.value.searchQuery
        if (query.isEmpty()) {
            _state.update {
                it.copy(
                    nbuList = nbuList.toPersistentList()
                )
            }
            return
        }

        _state.update {
            it.copy(
                nbuList = nbuList.filter { nbu ->
                    nbu.code.contains(query, ignoreCase = true) || nbu.title.contains(
                        query,
                        ignoreCase = true
                    )
                }.toPersistentList()
            )
        }
    }
}