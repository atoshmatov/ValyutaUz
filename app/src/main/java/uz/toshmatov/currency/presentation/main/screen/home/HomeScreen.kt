package uz.toshmatov.currency.presentation.main.screen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.uicompoenent.CcyLoading
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
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

        if (state.isLoading)
            CcyLoading()
        else
            HomeScreenContent(
                state = state,
            )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        stickyHeader {
            HomeHeader(title = string.home_cbu.resource)
        }
        items(
            items = state.cbuList.reversed().takeLast(10).reversed(),
            key = {
                it.code
            }
        ) { cbuModel ->
            CBUCurrencyItems(cbuModel = cbuModel)
        }
        stickyHeader {
            HomeHeader(title = string.home_nbu.resource)
        }
        items(
            items = state.nbuList.reversed(),
            key = {
                it.code
            }
        ) { nbuModel ->
            NBUCurrencyItems(nbuModel = nbuModel)
        }
        stickyHeader {
            HomeHeader(title = string.home_section_banks.resource)
        }
        items(
            items = state.exchangeRateList.takeLast(10),
            key = {
                it.bank
            }
        ) { nbuModel ->
            BankCurrencyItems(
                exchangeBankModel = nbuModel,
                cbu = state.cbuData
            )
        }
    }
}



