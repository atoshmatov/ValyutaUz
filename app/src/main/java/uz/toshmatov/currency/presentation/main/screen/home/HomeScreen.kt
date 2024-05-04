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
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.home.component.CurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.component.HomeHeader
import uz.toshmatov.currency.presentation.main.screen.home.component.NBUCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.intents.HomeEvents
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

        HomeScreenContent(
            state = state,
            reduce = viewModel::reduce,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    reduce: (HomeEvents) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
            stickyHeader {
                HomeHeader(title = "Markaziy Bank")
            }
            items(
                items = state.cbuList.reversed().takeLast(10).reversed(),
                key = {
                    it.id
                }
            ) { cbuModel ->
                CurrencyItems(cbuModel = cbuModel)
            }

            stickyHeader {
                HomeHeader(title = "Milliy Bank")
            }

            items(
                items = state.nbuList.takeLast(10).reversed(),
                key = {
                    it.code
                }
            ) { nbuModel ->
                NBUCurrencyItems(nbuModel = nbuModel)
            }

            stickyHeader {
                HomeHeader(title = "Banklar kesimida")
            }

            items(
                items = state.exchangeRateList,
                key = {
                    it.bank
                }
            ) { nbuModel ->
                CurrencyItems(nbuModel = nbuModel)
            }
        }
    }

