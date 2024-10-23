@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.screen.detail

import android.os.Parcelable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.parcelize.Parcelize
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.uicompoenent.CcyLoading
import uz.toshmatov.currency.core.uicompoenent.CurrencyTextField
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.data.enumclass.CurrencyType
import uz.toshmatov.currency.data.enumclass.CurrencyType.BANK
import uz.toshmatov.currency.data.enumclass.CurrencyType.CBU
import uz.toshmatov.currency.data.enumclass.CurrencyType.NBU
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailEvents
import uz.toshmatov.currency.presentation.main.screen.detail.intents.DetailState
import uz.toshmatov.currency.presentation.main.screen.detail.viewModel.BankDetailViewModel
import uz.toshmatov.currency.presentation.main.screen.detail.viewModel.CBUDetailViewModel
import uz.toshmatov.currency.presentation.main.screen.detail.viewModel.NBUDetailViewModel
import uz.toshmatov.currency.presentation.main.screen.home.component.BankCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.component.CBUCurrencyItems
import uz.toshmatov.currency.presentation.main.screen.home.component.NBUCurrencyItems

@Parcelize
class DetailScreen(
    private val currencyType: CurrencyType
) : AndroidScreen(), Parcelable {
    @Composable
    override fun Content() {
        val bankViewModel = getViewModel<BankDetailViewModel>()
        val cbuViewModel = getViewModel<CBUDetailViewModel>()
        val nbuViewModel = getViewModel<NBUDetailViewModel>()

        val state by bankViewModel.state.collectAsState()
        val cbuState by cbuViewModel.state.collectAsState()
        val nbuState by nbuViewModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        if (state.loading)
            CcyLoading()
        else {
            when (currencyType) {
                CBU -> {
                    CBUScreen(
                        state = cbuState,
                        searchQuery = { query -> cbuViewModel.reduce(DetailEvents.SearchQuery(query)) },
                        backClick = { navigator.pop() },
                        itemClick = { codeName, code, rate ->
                            navigator.push(ConverterScreen(codeName, code, rate))
                        }
                    )
                }

                NBU -> {
                    NBUScreen(
                        state = nbuState,
                        searchQuery = { query -> nbuViewModel.reduce(DetailEvents.SearchQuery(query)) },
                        backClick = { navigator.pop() },
                        itemClick = { codeName, code, rate ->
                            navigator.push(ConverterScreen(codeName, code, rate))
                        }
                    )

                }

                BANK -> {
                    BankScreen(
                        state = state,
                        cbuState = cbuState,
                        searchQuery = { query -> bankViewModel.reduce(DetailEvents.SearchQuery(query)) },
                        backClick = { navigator.pop() },
                        itemClick = { }
                    )

                }
            }
        }

        LaunchedEffect(currencyType) {
            when (currencyType) {
                CBU -> cbuViewModel.getCBUCurrencyList()
                NBU -> nbuViewModel.getNBUCurrencyList()
                BANK -> bankViewModel.getExchangeBankData()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
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
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        item(key = "top_bar") {
            TopBar(
                titleId = title,
                onBackClick = backClick,
                contentDescription = "back"
            )
        }
        stickyHeader {
            if (state.listSize > 10)
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

@Composable
fun NBUScreen(
    state: DetailState,
    modifier: Modifier = Modifier,
    searchQuery: (String) -> Unit,
    backClick: () -> Unit,
    itemClick: (codeName: String, code: String, rate: String) -> Unit
) {
    val title = string.home_nbu
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        item {
            TopBar(
                titleId = title,
                onBackClick = backClick,
                contentDescription = "back"
            )
        }
        stickyHeader {
            if (state.listSize > 10)
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
            items = state.nbuList.reversed(),
            key = { it.code }
        ) { nbuModel ->
            NBUCurrencyItems(
                nbuModel = nbuModel,
                nbuItemClick = {
                    itemClick(nbuModel.title, nbuModel.code, nbuModel.cbPrice)
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BankScreen(
    state: DetailState,
    cbuState: DetailState,
    modifier: Modifier = Modifier,
    searchQuery: (String) -> Unit,
    backClick: () -> Unit,
    itemClick: () -> Unit
) {
    val title = string.home_section_banks
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        contentPadding = PaddingValues(bottom = 8.dp)
    ) {
        item {
            TopBar(
                titleId = title,
                onBackClick = backClick,
                contentDescription = "back"
            )
        }
        stickyHeader {
            if (state.listSize > 10)
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
            items = state.exchangeRateList,
            key = { it.bank }
        ) { nbuModel ->
            BankCurrencyItems(
                exchangeBankModel = nbuModel,
                cbu = cbuState.cbuData,
                bankItemClick = itemClick
            )
        }
    }
}