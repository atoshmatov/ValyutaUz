package uz.toshmatov.currency.presentation.main.screen.detail.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.model.ExchangeBankModel
import uz.toshmatov.currency.domain.model.NBUModel

data class DetailState(
    val loading: Boolean = false,
    val error: String = "",
    val cbuData: String = "",
    val listSize: Int = 0,
    val searchQuery: String = "",
    val cbuList: ImmutableList<CBUModel> = persistentListOf(),
    val nbuList: ImmutableList<NBUModel> = persistentListOf(),
    val exchangeRateList: ImmutableList<ExchangeBankModel> = persistentListOf()
)
