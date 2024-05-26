package uz.toshmatov.currency.presentation.main.screen.home.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.model.ExchangeModel
import uz.toshmatov.currency.domain.model.NBUModel

data class HomeState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cbuData: String = "",
    val cbuList: ImmutableList<CBUModel> = persistentListOf(),
    val nbuList: ImmutableList<NBUModel> = persistentListOf(),
    val exchangeRateList: ImmutableList<ExchangeModel> = persistentListOf()
)
