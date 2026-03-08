package uz.toshmatov.currency.presentation.main.tabscreen.home.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.core.connect.ConnectivityObserver
import uz.toshmatov.currency.domain.model.CBUModel

data class HomeState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cbuData: String = "",
    val isEmptyCbuList: Boolean = false,
    val isDataStale: Boolean = false,
    val lastUpdateTimestamp: Long = 0L,
    //val networkStatus: ConnectivityObserver.Status = ConnectivityObserver.Status.UNAVAILABLE,
    val cbuList: ImmutableList<CBUModel> = persistentListOf(),
)
