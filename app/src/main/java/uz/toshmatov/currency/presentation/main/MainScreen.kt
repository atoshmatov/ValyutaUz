@file:OptIn(ExperimentalVoyagerApi::class)

package uz.toshmatov.currency.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import uz.toshmatov.currency.core.ads.AdBanner
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.tabscreen.home.HomeScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.SettingScreen

class MainScreen(
    private val initialConverterCodeName: String? = null,
    private val initialConverterCode: String? = null,
    private val initialConverterRate: String? = null
) : Screen {
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
            content = { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = padding.calculateBottomPadding())
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                AdBanner(modifier = Modifier.fillMaxWidth())
            },
        )
    }
}
