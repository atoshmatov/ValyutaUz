@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyTextField
import uz.toshmatov.currency.core.uicompoenent.ShimmedList
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import uz.toshmatov.currency.presentation.main.screen.detail.viewModel.CBUDetailViewModel
import uz.toshmatov.currency.presentation.main.tabscreen.home.component.CBUCurrencyItems

class DetailScreen : Screen {

    @Composable
    override fun Content() {
        val cbuViewModel = getViewModel<CBUDetailViewModel>()

        val cbuState by cbuViewModel.state.collectAsStateWithLifecycle()

        val navigator = LocalNavigator.currentOrThrow

        if (cbuState.loading) {
            ShimmedList()
        } else if (cbuState.error.isNotEmpty() && cbuState.cbuList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CurrencyColors.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cbuState.error,
                    color = CurrencyColors.text,
                    style = CurrencyTypography.textMedium
                )
            }
        } else {
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
            .background(CurrencyColors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
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
        item {
            Spacer(Modifier.height(CurrencyDimensions.medium))
        }
    }
}
