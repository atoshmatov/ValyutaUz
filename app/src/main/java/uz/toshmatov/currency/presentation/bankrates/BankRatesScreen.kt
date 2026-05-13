package uz.toshmatov.currency.presentation.bankrates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.domain.model.BankRateModel

private val SUPPORTED_CURRENCIES = listOf("USD", "EUR", "RUB", "GBP", "CNY", "JPY")

class BankRatesScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<BankRatesViewModel>()
        val state by viewModel.state.collectAsState()

        BankRatesContent(
            state = state,
            onCurrencySelect = viewModel::load,
            onBack = navigator::pop
        )
    }
}

@Composable
private fun BankRatesContent(
    state: BankRatesState,
    onCurrencySelect: (String) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
    ) {
        TopBar(
            titleId = uz.toshmatov.currency.core.utils.string.bank_rates,
            onBackClick = onBack,
            contentDescription = "back"
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SUPPORTED_CURRENCIES) { currency ->
                CurrencyChip(
                    label = currency,
                    selected = state.selectedCurrency == currency,
                    onClick = { onCurrencySelect(currency) }
                )
            }
        }

        when {
            state.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CurrencyColors.button)
                }
            }
            state.error.isNotEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.error, color = CurrencyColors.error, style = CurrencyTypography.textMedium)
                }
            }
            state.rates.isNotEmpty() -> {
                val maxBuy = state.rates.maxOf { it.buy }
                val minSell = state.rates.minOf { it.sell }

                BestRatesCard(
                    highestBuy = state.rates.first { it.buy == maxBuy },
                    lowestSell = state.rates.first { it.sell == minSell },
                    currency = state.selectedCurrency
                )

                RatesHeader()

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.rates.sortedByDescending { it.buy }) { rate ->
                        BankRateRow(
                            rate = rate,
                            isHighestBuy = rate.buy == maxBuy,
                            isLowestSell = rate.sell == minSell
                        )
                    }
                    item { Spacer(Modifier.height(CurrencyDimensions.medium)) }
                }
            }
        }
    }
}

@Composable
private fun BestRatesCard(
    highestBuy: BankRateModel,
    lowestSell: BankRateModel,
    currency: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BestRateItem(
            modifier = Modifier.weight(1f),
            label = "Eng yuqori sotib olish",
            bankName = highestBuy.bankName,
            rate = highestBuy.buy,
            currency = currency,
            color = CurrencyColors.success
        )
        BestRateItem(
            modifier = Modifier.weight(1f),
            label = "Eng past sotish",
            bankName = lowestSell.bankName,
            rate = lowestSell.sell,
            currency = currency,
            color = CurrencyColors.button
        )
    }
}

@Composable
private fun BestRateItem(
    modifier: Modifier = Modifier,
    label: String,
    bankName: String,
    rate: Int,
    currency: String,
    color: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.1f))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(label, style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
        Spacer(Modifier.height(4.dp))
        Text(
            text = "%,d so'm".format(rate),
            style = CurrencyTypography.textSemiBold,
            color = color
        )
        Text(
            text = bankName,
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun RatesHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(CurrencyColors.itemBackground)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Bank", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary, modifier = Modifier.weight(1f))
        Text("Sotib olish", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
        Spacer(Modifier.size(40.dp))
        Text("Sotish", style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
    }
}

@Composable
private fun BankRateRow(
    rate: BankRateModel,
    isHighestBuy: Boolean,
    isLowestSell: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = rate.bankName,
            style = CurrencyTypography.textMedium,
            color = CurrencyColors.text,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "%,d".format(rate.buy),
            style = CurrencyTypography.textSemiBold,
            color = if (isHighestBuy) CurrencyColors.success else CurrencyColors.text,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            text = "%,d".format(rate.sell),
            style = CurrencyTypography.textSemiBold,
            color = if (isLowestSell) CurrencyColors.button else CurrencyColors.text
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(0.5.dp)
            .background(CurrencyColors.itemBackground)
    )
}

@Composable
private fun CurrencyChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val accent = CurrencyColors.button
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) accent else CurrencyColors.itemBackground)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = CurrencyTypography.textSemiBold,
            color = if (selected) Color.White else CurrencyColors.textSecondary
        )
    }
}
