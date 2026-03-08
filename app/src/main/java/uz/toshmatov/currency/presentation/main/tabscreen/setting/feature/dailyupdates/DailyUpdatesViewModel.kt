package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates.intents.DailyUpdatesState
import uz.toshmatov.currency.work.CurrencyWorkScheduler
import javax.inject.Inject

@HiltViewModel
class DailyUpdatesViewModel @Inject constructor(
    private val cbuRepository: CBURepository,
    private val dataStoreRepository: DataStoreRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(DailyUpdatesState())
    val state: StateFlow<DailyUpdatesState> = _state.asStateFlow()

    init {
        observeSettings()
        observeCurrencies()
    }

    fun toggleCurrency(code: String) {
        val current = _state.value.selectedCodes
        val updated = if (current.contains(code)) {
            current - code
        } else {
            current + code
        }
        viewModelScope.launch {
            dataStoreRepository.setSelectedWidgetCodes(updated)
            scheduleDailyWork(updated, _state.value.notificationTime)
            CurrencyWorkScheduler.enqueueImmediateUpdate(
                context = context,
                sendNotification = false
            )
        }
    }

    fun updateNotificationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.setDailyNotificationEnabled(enabled)
            scheduleDailyWork(_state.value.selectedCodes, _state.value.notificationTime)
            if (enabled && _state.value.selectedCodes.isNotEmpty()) {
                CurrencyWorkScheduler.enqueueImmediateUpdate(
                    context = context,
                    sendNotification = true
                )
            }
        }
    }

    fun updateNotificationTime(time: String) {
        viewModelScope.launch {
            dataStoreRepository.setDailyNotificationTime(time)
            scheduleDailyWork(_state.value.selectedCodes, time)
            if (_state.value.notificationEnabled && _state.value.selectedCodes.isNotEmpty()) {
                CurrencyWorkScheduler.enqueueImmediateUpdate(
                    context = context,
                    sendNotification = true
                )
            }
        }
    }

    private fun observeSettings() {
        combine(
            dataStoreRepository.getSelectedWidgetCodes(),
            dataStoreRepository.getDailyNotificationEnabled(),
            dataStoreRepository.getDailyNotificationTime()
        ) { selectedCodes, notificationEnabled, notificationTime ->
            Triple(selectedCodes, notificationEnabled, notificationTime)
        }.onEach { (selectedCodes, notificationEnabled, notificationTime) ->
            _state.update { state ->
                state.copy(
                    selectedCodes = selectedCodes,
                    notificationEnabled = notificationEnabled,
                    notificationTime = notificationTime
                )
            }
            scheduleDailyWork(selectedCodes, notificationTime)
        }.launchIn(viewModelScope)
    }

    private fun observeCurrencies() {
        cbuRepository.getCBUCurrencyList()
            .onEach { list ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        error = "",
                        currencyList = list
                            .distinctBy { it.ccy }
                            .sortedBy { it.ccy }
                            .toPersistentList()
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun scheduleDailyWork(selectedCodes: Set<String>, notificationTime: String) {
        CurrencyWorkScheduler.scheduleDaily(
            context = context,
            time = notificationTime,
            hasSelectedCurrencies = selectedCodes.isNotEmpty()
        )
    }
}
