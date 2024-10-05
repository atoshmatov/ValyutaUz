@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import kotlinx.coroutines.launch
import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.uicompoenent.CcyLoading
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.data.enumclass.CurrencyType
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.detail.DetailScreen
import uz.toshmatov.currency.presentation.main.screen.home.component.BankCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.component.CBUCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.component.HomeHeader
import uz.toshmatov.currency.presentation.main.screen.home.component.NBUCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.intents.HomeState

object HomeScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon =
                rememberVectorPainter(ImageVector.vectorResource(id = drawable.ic_tab_home))
            val title = string.tab_home.resource

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel = getViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()
        val currentNavigator = LocalNavigator.currentOrThrow.parent!!

        if (state.isLoading)
            CcyLoading()
        else
            HomeScreenContent(
                state = state,
                onClickSeeAll = { currencyType ->
                    currentNavigator.push(DetailScreen(currencyType))
                },
                itemClick = { codeName, code, rate ->
                    currentNavigator.push(ConverterScreen(codeName, code, rate))
                }
            )
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onClickSeeAll: (CurrencyType) -> Unit = {},
    itemClick: (codeName: String, code: String, rate: String) -> Unit,
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(listState) {
        derivedStateOf {
            scope.launch {
                listState.animateScrollToItem(
                    index = listState.firstVisibleItemIndex,
                    scrollOffset = listState.firstVisibleItemScrollOffset
                )
            }
        }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        state = listState
    ) {
        stickyHeader(key = CurrencyType.CBU) {
            HomeHeader(
                title = string.home_cbu.resource,
                onClick = { onClickSeeAll(CurrencyType.CBU) }
            )
        }
        items(
            items = state.cbuList
                .reversed()
                .takeLast(7)
                .reversed(),
            key = {
                it.code
            }
        ) { cbuModel ->
            CBUCurrencyItems(
                cbuModel = cbuModel,
                cbuItemClick = {
                    itemClick(cbuModel.ccyName, cbuModel.ccy, cbuModel.rate)
                }
            )
        }
        stickyHeader(key = CurrencyType.NBU) {
            HomeHeader(
                title = string.home_nbu.resource,
                onClick = { onClickSeeAll(CurrencyType.NBU) })
        }
        items(
            items = state.nbuList
                .reversed()
                .takeLast(7),
            key = {
                it.code
            }
        ) { nbuModel ->
            NBUCurrencyItems(
                nbuModel = nbuModel,
                nbuItemClick = {
                    itemClick(
                        nbuModel.title,
                        nbuModel.code,
                        nbuModel.cbPrice.toNumber().toSom()
                    )
                }
            )
        }
        stickyHeader(key = CurrencyType.BANK) {
            HomeHeader(
                title = string.home_section_banks.resource,
                onClick = { onClickSeeAll(CurrencyType.BANK) })
        }
        items(
            items = state.exchangeRateList
                .reversed()
                .takeLast(7)
                .reversed(),
            key = {
                it.bank
            }
        ) { nbuModel ->
            BankCurrencyItems(
                exchangeBankModel = nbuModel,
                cbu = state.cbuData,
                bankItemClick = {}
            )
        }
    }
}