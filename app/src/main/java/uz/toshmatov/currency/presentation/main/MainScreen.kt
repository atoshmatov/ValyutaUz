@file:OptIn(ExperimentalVoyagerApi::class)

package uz.toshmatov.currency.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.presentation.main.screen.home.HomeScreen
import uz.toshmatov.currency.presentation.main.screen.setting.SettingScreen

class MainScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        MainScreenContent()
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier
) {
    val tabs = listOf(HomeScreen, SettingScreen)

    TabNavigator(
        HomeScreen,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = tabs,
            )
        },
    ) {

        Scaffold(
            modifier = modifier,
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) { CurrentTab() }
            },
            bottomBar = {
                Column {
                    HorizontalDivider(color = CurrencyColors.shimmer, thickness = 1.dp)
                    BottomNavigatorBar(tabs = tabs.toPersistentList())
                }
            },
        )
    }
}

@Composable
private fun BottomNavigatorBar(tabs: ImmutableList<Tab>) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBar(
        containerColor = CurrencyColors.bottomBar,
    ) {
        tabs.forEach { tab ->
            val icon = tab.options.icon ?: return@forEach

            NavigationBarItem(
                selected = tabNavigator.current.key == tab.key,
                onClick = { tabNavigator.current = tab },
                label = { Text(tab.options.title) },
                icon = { Icon(painter = icon, contentDescription = null) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CurrencyColors.bottomBarIconSelected,
                    unselectedIconColor = CurrencyColors.textSecondary,
                    selectedTextColor = CurrencyColors.bottomBarTextSelected,
                    unselectedTextColor = CurrencyColors.bottomBarText,
                    indicatorColor = CurrencyColors.bottomBarIndicator,
                ),
            )
        }
    }
}