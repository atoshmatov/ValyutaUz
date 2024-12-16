package uz.toshmatov.currency.presentation.main.screen.detail.intents

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import uz.toshmatov.currency.domain.model.CBUModel

data class DetailState(
    val loading: Boolean = false,
    val error: String = "",
    val cbuData: String = "",
    val searchQuery: String = "",
    val cbuList: ImmutableList<CBUModel> = persistentListOf(),
)
