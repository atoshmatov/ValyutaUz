@file:OptIn(ExperimentalVoyagerApi::class)

package uz.toshmatov.currency.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.tabscreen.home.HomeScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.SettingScreen

class MainScreen(
    private val initialConverterCodeName: String? = null,
    private val initialConverterCode: String? = null,
    private val initialConverterRate: String? = null
) : AndroidScreen() {
    @Composable
    override fun Content() {
        MainScreenContent(
            initialConverterCodeName = initialConverterCodeName,
            initialConverterCode = initialConverterCode,
            initialConverterRate = initialConverterRate
        )
    }
}

@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    initialConverterCodeName: String? = null,
    initialConverterCode: String? = null,
    initialConverterRate: String? = null
) {
    val tabs = listOf(HomeScreen, SettingScreen)

    TabNavigator(
        tab = HomeScreen,
        tabDisposable = {
            TabDisposable(
                navigator = it,
                tabs = tabs,
            )
        },
    ) {
        val tabNavigator = LocalTabNavigator.current
        val parentNavigator = LocalNavigator.currentOrThrow.parent
        var converterLaunchHandled by rememberSaveable(
            initialConverterCodeName,
            initialConverterCode,
            initialConverterRate
        ) { mutableStateOf(false) }

        LaunchedEffect(
            initialConverterCodeName,
            initialConverterCode,
            initialConverterRate,
            converterLaunchHandled
        ) {
            val code = initialConverterCode.orEmpty()
            val rate = initialConverterRate.orEmpty()
            if (
                converterLaunchHandled ||
                code.isBlank() ||
                rate.isBlank()
            ) {
                return@LaunchedEffect
            }
            converterLaunchHandled = true
            tabNavigator.current = HomeScreen
            parentNavigator?.push(
                ConverterScreen(
                    codeName = initialConverterCodeName.orEmpty().ifBlank { code },
                    code = code,
                    rate = rate
                )
            )
        }

        BackHandler(enabled = tabNavigator.current != HomeScreen) {
            tabNavigator.current = HomeScreen
        }

        Scaffold(
            modifier = modifier,
            content = { _ ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                BottomNavigatorBar(tabs = tabs.toPersistentList())
            },
        )
    }
}

@Composable
private fun BottomNavigatorBar(tabs: ImmutableList<Tab>) {
    val tabNavigator = LocalTabNavigator.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CurrencyColors.bottomBar,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        tonalElevation = 4.dp,
        shadowElevation = 10.dp
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth(),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
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
                        indicatorColor = CurrencyColors.button.copy(alpha = 0.16f),
                    ),
                )
            }
        }
    }
}
