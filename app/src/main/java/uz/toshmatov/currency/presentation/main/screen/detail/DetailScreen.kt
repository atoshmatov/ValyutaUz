@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.uicompoenent.CurrencyLoading
import uz.toshmatov.currency.core.uicompoenent.CurrencyTextField
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import uz.toshmatov.currency.presentation.main.screen.detail.viewModel.CBUDetailViewModel
import uz.toshmatov.currency.presentation.main.tabscreen.home.component.CBUCurrencyItems

class DetailScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val cbuViewModel = getViewModel<CBUDetailViewModel>()

        val cbuState by cbuViewModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        if (cbuState.loading)
            CurrencyLoading()
        else {
            CBUScreen(
                state = cbuState,
                searchQuery = { query -> cbuViewModel.reduce(DetailEvents.SearchQuery(query)) },
                backClick = { navigator.pop() },
                itemClick = { codeName, code, rate ->
                    navigator.push(ConverterScreen(codeName, code, rate))
                }
            )
        }
    }
}

@Composable
fun CBUScreen(
    state: DetailState,
    modifier: Modifier = Modifier,
    searchQuery: (String) -> Unit,
    backClick: () -> Unit,
    itemClick: (codeName: String, code: String, rate: String) -> Unit
) {
    val title = string.home_cbu
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        item(key = "top_bar") {
            TopBar(
                titleId = title,
                onBackClick = backClick,
                contentDescription = "back"
            )
        }
        stickyHeader {
            CurrencyTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CurrencyColors.background)
                    .padding(
                        horizontal = CurrencyDimensions.medium,
                        vertical = CurrencyDimensions.small
                    ),
                onValueChange = searchQuery,
            )
        }
        items(
            items = state.cbuList,
            key = { it.code }
        ) { cbuModel ->
            CBUCurrencyItems(
                cbuModel = cbuModel,
                cbuItemClick = {
                    itemClick(cbuModel.ccyName, cbuModel.ccy, cbuModel.rate)
                }
            )
        }
    }
}