package uz.toshmatov.currency.presentation.main.screen.setting.feature.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.component.ThemeItem
import uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.intents.ThemeEvents
import uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.intents.ThemeState

class ThemeScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<ThemeViewModel>()
        val state by viewModel.state.collectAsState()
        val currentNavigator = LocalNavigator.current

        ThemeContent(
            state = state,
            reduce = viewModel::reduce
        )
    }
}

@Composable
fun ThemeContent(
    state: ThemeState,
    reduce: (ThemeEvents) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.themeList.forEach {
            ThemeItem(
                themeModel = it,
                onClick = { mode ->
                    reduce(ThemeEvents.UpdateTheme(mode))
                },
                isCheck = state.currentThemeMode == it.themeMode
            )
        }
    }
}