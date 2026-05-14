@file:OptIn(ExperimentalFoundationApi::class)

package uz.toshmatov.currency.presentation.main.tabscreen.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import uz.toshmatov.currency.presentation.bankrates.BankRatesScreen
import uz.toshmatov.currency.presentation.main.screen.detail.CurrencyDetailScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.SettingScreen
import com.amurfm.android.core.networkConnect.NetworkConnectionState
import com.amurfm.android.core.networkConnect.rememberConnectivityState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.uicompoenent.ShimmedList
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
            val icon = painterResource(id = drawable.ic_tab_home)
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
        val tabNavigator = LocalTabNavigator.current

        val internetConnection = rememberConnectivityState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isLoading)

        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.refresh() }
        ) {
            val isOffline = internetConnection.value == NetworkConnectionState.LOST ||
                internetConnection.value == NetworkConnectionState.Unavailable ||
                internetConnection.value == NetworkConnectionState.LOSING

            when {
                state.isLoading && state.cbuList.isEmpty() -> ShimmedList()
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
                isOffline && state.isEmptyCbuList -> {
                    EmptyScreen(string.empty.resource)
                }
                else -> {
                    HomeScreenContent(
                        state = state,
                        onSettingsClick = { tabNavigator.current = SettingScreen },
                        onBankRatesClick = { currentNavigator.push(BankRatesScreen()) },
                        onClickSeeAll = { currentNavigator.push(DetailScreen()) },
                        itemClick = { codeName, code, rate ->
                            currentNavigator.push(CurrencyDetailScreen(codeName, code, rate))
                        },
                        showOfflineStaleWarning = isOffline && state.isDataStale && state.cbuList.isNotEmpty(),
                        onRefreshData = viewModel::refresh
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
    onSettingsClick: () -> Unit = {},
    onBankRatesClick: () -> Unit = {},
    onClickSeeAll: () -> Unit = {},
    itemClick: (codeName: String, code: String, rate: String) -> Unit,
    showOfflineStaleWarning: Boolean = false,
    onRefreshData: () -> Unit = {},
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
            .statusBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        stickyHeader(key = "CBU") {
            HomeHeader(
                title = string.home_cbu.resource,
                onSettingsClick = onSettingsClick,
                onBankRatesClick = onBankRatesClick,
                isDataStale = state.isDataStale
            )
        }
        if (showOfflineStaleWarning) {
            item(key = "stale_warning") {
                StaleDataWarningCard(onRefreshData = onRefreshData)
            }
        }
        items(
            items = state.cbuList,
            key = { it.code }
        ) { cbuModel ->
            CBUCurrencyItems(
                cbuModel = cbuModel,
                isDataStale = state.isDataStale,
                cbuItemClick = {
                    itemClick(cbuModel.ccyName, cbuModel.ccy, cbuModel.rate)
                }
            )
        }
        item(key = "see_all") {
            TextButton(
                onClick = onClickSeeAll,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = string.home_all_item.resource,
                    color = CurrencyColors.button,
                    style = CurrencyTypography.textSemiBold
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = CurrencyColors.button
                )
            }
        }
        item(key = "bottom_space") {
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun StaleDataWarningCard(
    onRefreshData: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = if (isDarkTheme) 1.dp else 0.8.dp,
            color = CurrencyColors.error.copy(alpha = if (isDarkTheme) 0.45f else 0.25f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = CurrencyColors.error.copy(alpha = if (isDarkTheme) 0.16f else 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = string.home_stale_title.resource,
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = string.home_stale_offline_message.resource,
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionRegular
            )
            androidx.compose.material3.TextButton(
                onClick = onRefreshData,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = string.home_stale_refresh.resource,
                    color = CurrencyColors.button,
                    style = CurrencyTypography.textSemiBold
                )
            }
        }
    }
}
