package uz.toshmatov.currency.presentation.main.screen.converter

import android.annotation.SuppressLint
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
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
            rate = rate
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ConverterScreenContent(
    backClick: () -> Unit,
    codeName: String,
    code: String = "",
    rate: String = ""
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
            onBackClick = backClick
        )
        ConverterItem(
            modifier = Modifier
                .height(120.dp)
                .padding(horizontal = CurrencyDimensions.medium),
            number = number.ifEmpty { "0" }.plus(" $code"),
            codeName = codeName,
            rate = rate
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
        ConverterItem(
            modifier = Modifier
                .height(120.dp)
                .padding(horizontal = CurrencyDimensions.medium),
            number = (number.convertSomToDouble() * rate.convertSomToDouble())
                .formatNumberDynamically().toNumber2().ifEmpty { "0" }.plus(" so'm"),
            rate = (1 / rate.convertSomToDouble()).formatNumberDynamically()
                .plus(" $code")
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

fun main() {
    val totalTime = runBlocking {
        val time1 = async { delayFunction1() }
        val time2 = async { delayFunction2() }
        val result1 = time1.await()
        val result2 = time2.await()
        println("Total time: ${result1 + result2}")
    }
    println("Done")
}

suspend fun delayFunction1(): Long {
    val delayTime = 1000L
    delay(delayTime)
    return delayTime
}

suspend fun delayFunction2(): Long {
    val delayTime = 500L
    delay(delayTime)
    return delayTime
}