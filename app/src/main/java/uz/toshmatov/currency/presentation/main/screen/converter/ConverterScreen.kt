package uz.toshmatov.currency.presentation.main.screen.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.extensions.convertSomToDouble
import uz.toshmatov.currency.core.extensions.formatNumberDynamically
import uz.toshmatov.currency.core.extensions.toNumber2
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.converter.component.ConverterItem
import uz.toshmatov.currency.presentation.main.screen.converter.component.NumberGridItem

class ConverterScreen(
    val codeName: String,
    val code: String,
    val rate: String
) : AndroidScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ConverterScreenContent(
            backClick = navigator::pop,
            codeName = codeName,
            code = code,
            rate = rate,
        )
    }
}

@Composable
fun ConverterScreenContent(
    backClick: () -> Unit,
    codeName: String,
    code: String = "",
    rate: String = "",
) {
    var number by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(
            titleId = string.tab_converter,
            onBackClick = backClick,
            contentDescription = "back"
        )
        ConverterItem(
            modifier = Modifier
                .padding(horizontal = CurrencyDimensions.medium),
            number = number.ifEmpty { "0" }.plus(" $code"),
            codeName = codeName,
            rate = rate
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
        ConverterItem(
            modifier = Modifier
                .padding(horizontal = CurrencyDimensions.medium),
            number = (number.convertSomToDouble() * rate.convertSomToDouble())
                .formatNumberDynamically().toNumber2().plus(" so'm"),
            rate = (1 / rate.convertSomToDouble()).formatNumberDynamically()
                .plus(" $code"),
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )
        NumberGridItem(
            numberClick = { number = it }
        )
        Spacer(
            modifier = Modifier.height(CurrencyDimensions.medium)
        )
    }
}