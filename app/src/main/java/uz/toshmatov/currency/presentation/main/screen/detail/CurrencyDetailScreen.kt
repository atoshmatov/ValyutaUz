package uz.toshmatov.currency.presentation.main.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.domain.model.BankRateModel
import uz.toshmatov.currency.presentation.bankrates.BankRatesViewModel
import uz.toshmatov.currency.presentation.main.screen.converter.ConverterScreen
import uz.toshmatov.currency.presentation.main.screen.converter.chart.ChartViewModel
import uz.toshmatov.currency.presentation.main.screen.converter.chart.CurrencyChartSection

class CurrencyDetailScreen(
    private val codeName: String,
    private val code: String,
    private val rate: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chartViewModel = getViewModel<ChartViewModel>()
        val bankRatesViewModel = getViewModel<BankRatesViewModel>()

        LaunchedEffect(code) {
            bankRatesViewModel.load(code)
        }

        val bankState by bankRatesViewModel.state.collectAsState()

        CurrencyDetailContent(
            codeName = codeName,
            code = code,
            rate = rate,
            chartViewModel = chartViewModel,
            bankRates = bankState.rates,
            isBankLoading = bankState.isLoading,
            onBack = navigator::pop,
            onOpenConverter = {
                navigator.push(ConverterScreen(codeName, code, rate))
            }
        )
    }
}

@Composable
private fun CurrencyDetailContent(
    codeName: String,
    code: String,
    rate: String,
    chartViewModel: ChartViewModel,
    bankRates: List<BankRateModel>,
    isBankLoading: Boolean,
    onBack: () -> Unit,
    onOpenConverter: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(CurrencyColors.background)) {
        TopBar(title = "$codeName ($code)", onBackClick = onBack, contentDescription = "back")

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item(key = "rate_card") {
                RateCard(codeName = codeName, code = code, rate = rate)
            }
            item(key = "chart") {
                Spacer(Modifier.height(8.dp))
                CurrencyChartSection(
                    currencyCode = code,
                    viewModel = chartViewModel,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Spacer(Modifier.height(16.dp))
            }
            item(key = "bank_header") {
                Text(
                    text = "Bank kurslari ($code)",
                    style = CurrencyTypography.textSemiBold,
                    color = CurrencyColors.text,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            when {
                isBankLoading -> item { Box(Modifier.fillMaxWidth().height(80.dp), Alignment.Center) { CircularProgressIndicator(color = CurrencyColors.button, strokeWidth = 2.dp) } }
                bankRates.isEmpty() -> item { Text("Ma'lumot topilmadi", style = CurrencyTypography.textMedium, color = CurrencyColors.textSecondary, modifier = Modifier.padding(16.dp)) }
                else -> {
                    val maxBuy = bankRates.maxOf { it.buy }
                    val minSell = bankRates.minOf { it.sell }
                    item(key = "bank_col_header") { BankColumnHeader() }
                    items(bankRates.sortedByDescending { it.buy }, key = { it.bankSlug }) { rate ->
                        BankRateRow(rate, rate.buy == maxBuy, rate.sell == minSell)
                    }
                }
            }
            item { Spacer(Modifier.height(100.dp)) }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(CurrencyColors.bottomBar)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = onOpenConverter,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CurrencyColors.button)
            ) {
                Text("Kalkulatorda hisoblash", style = CurrencyTypography.textSemiBold, color = Color.White)
            }
        }
    }
}

@Composable
private fun RateCard(codeName: String, code: String, rate: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(CurrencyColors.itemBackground, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(text = codeName, style = CurrencyTypography.textMedium, color = CurrencyColors.textSecondary)
        Spacer(Modifier.height(4.dp))
        Text(text = "$rate so'm", style = CurrencyTypography.buttonRegular, color = CurrencyColors.button)
        Text(text = "Markaziy bank kursi", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
    }
}

@Composable
private fun BankColumnHeader() {
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(CurrencyColors.itemBackground)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Bank", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary, modifier = Modifier.weight(1f))
        Text("Sotib olish", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary, modifier = Modifier.padding(end = 20.dp))
        Text("Sotish", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
    }
}

@Composable
private fun BankRateRow(rate: BankRateModel, isHighestBuy: Boolean, isLowestSell: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(rate.bankName, style = CurrencyTypography.textMedium, color = CurrencyColors.text, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text("%,d".format(rate.buy), style = CurrencyTypography.textSemiBold, color = if (isHighestBuy) CurrencyColors.success else CurrencyColors.text, modifier = Modifier.padding(end = 20.dp))
        Text("%,d".format(rate.sell), style = CurrencyTypography.textSemiBold, color = if (isLowestSell) CurrencyColors.button else CurrencyColors.text)
    }
    Box(Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(0.5.dp).background(CurrencyColors.itemBackground))
}
