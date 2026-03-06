@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.tabscreen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.amurfm.android.core.networkConnect.NetworkConnectionState
import com.amurfm.android.core.networkConnect.rememberConnectivityState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.ShimmedList
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.empty.EmptyScreen
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.detail.DetailScreen
import uz.toshmatov.currency.presentation.main.tabscreen.home.component.CBUCurrencyItems
import uz.toshmatov.currency.presentation.main.tabscreen.home.component.HomeHeader
import uz.toshmatov.currency.presentation.main.tabscreen.home.intents.HomeState

object HomeScreen : Tab {
    private fun readResolve(): Any = HomeScreen

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
        val state by viewModel.state.collectAsStateWithLifecycle()
        val currentNavigator = LocalNavigator.currentOrThrow.parent!!

        val internetConnection = rememberConnectivityState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refresh() }
        ) {
            when {
                state.isLoading -> ShimmedList()
                state.error.isNotEmpty() && state.cbuList.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(CurrencyColors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error,
                            color = CurrencyColors.text,
                            style = CurrencyTypography.textMedium
                        )
                    }
                }
                (internetConnection.value == NetworkConnectionState.LOST ||
                    internetConnection.value == NetworkConnectionState.Unavailable ||
                    internetConnection.value == NetworkConnectionState.LOSING) &&
                    state.isEmptyCbuList -> {
                    EmptyScreen(string.empty.resource)
                }
                else -> {
                    HomeScreenContent(
                        state = state,
                        onClickSeeAll = {
                            currentNavigator.push(DetailScreen())
                        },
                        itemClick = { codeName, code, rate ->
                            currentNavigator.push(ConverterScreen(codeName, code, rate))
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    state: HomeState,
    onClickSeeAll: () -> Unit = {},
    itemClick: (codeName: String, code: String, rate: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        stickyHeader(key = "CBU") {
            HomeHeader(
                title = string.home_cbu.resource,
                onClick = { onClickSeeAll() }
            )
        }
        items(
            items = state.cbuList,
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
        item {
            Spacer(Modifier.height(80.dp))
        }
    }
}
