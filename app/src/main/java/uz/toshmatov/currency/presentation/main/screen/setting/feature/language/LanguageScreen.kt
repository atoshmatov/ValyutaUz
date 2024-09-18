package uz.toshmatov.currency.presentation.main.screen.setting.feature.language

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.component.LanguageItem
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.intents.LanguageState
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.lingver.LingverLocalization

class LanguageScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<LanguageViewModel>()
        val state by viewModel.state.collectAsState()
        val navigation = LocalNavigator.currentOrThrow

        LanguageContent(
            state = state,
            onClose = navigation::pop,
            backClick = navigation::pop
        )
    }
}

@Composable
fun LanguageContent(
    modifier: Modifier = Modifier,
    state: LanguageState,
    onClose: () -> Unit,
    backClick: () -> Unit
) {
    val context = LocalContext.current

    val onLanguageSelected: (String) -> Unit = {
        LingverLocalization().setLocale(context, it)
        onClose()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            titleId = string.settings_language,
            onBackClick = backClick
        )
        state.languageList.forEach { languageModel ->
            LanguageItem(
                languageModel = languageModel,
                onLanguageSelected = onLanguageSelected
            )
        }
    }
}