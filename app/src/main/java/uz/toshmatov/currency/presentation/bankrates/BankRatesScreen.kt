package uz.toshmatov.currency.presentation.bankrates

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.text.style.TextAlign
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
import uz.toshmatov.currency.core.utils.string
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
        TopBar(titleId = string.bank_rates, onBackClick = onBack, contentDescription = "back")

        // Currency chips — contentPadding prevents clipping at edges
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
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
                    Text(
                        state.error,
                        color = CurrencyColors.error,
                        style = CurrencyTypography.textMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
            state.rates.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyState(currency = state.selectedCurrency)
                }
            }
            else -> {
                val maxBuy = state.rates.maxOf { it.buy }
                val minSell = state.rates.minOf { it.sell }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item(key = "best") {
                        BestRatesCard(
                            highestBuy = state.rates.first { it.buy == maxBuy },
                            lowestSell = state.rates.first { it.sell == minSell },
                            currency = state.selectedCurrency
                        )
                    }
                    item(key = "header") { RatesHeader() }
                    items(state.rates.sortedByDescending { it.buy }, key = { it.bankSlug }) { rate ->
                        BankRateCard(
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
private fun EmptyState(currency: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(drawable.ic_time),
            contentDescription = null,
            tint = CurrencyColors.textSecondary,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = "$currency uchun bank kurslari topilmadi",
            style = CurrencyTypography.textMedium,
            color = CurrencyColors.textSecondary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Ushbu valyuta uchun banklar kurs belgilamagan bo'lishi mumkin",
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.textSecondary.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
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
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BestRateItem(
            modifier = Modifier.weight(1f),
            label = "Eng yuqori olish",
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
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(12.dp)
    ) {
        Text(label, style = CurrencyTypography.captionRegular, color = CurrencyColors.textSecondary)
        Spacer(Modifier.height(6.dp))
        Text(
            text = "%,d".format(rate),
            style = CurrencyTypography.textSemiBold,
            color = color
        )
        Text(
            text = "so'm",
            style = CurrencyTypography.captionRegular,
            color = color.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(4.dp))
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
            .padding(horizontal = 16.dp)
            .padding(top = 4.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Bank",
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.textSecondary,
            modifier = Modifier.weight(1f)
        )
        Text(
            "Olish",
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.textSecondary,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(
            "Sotish",
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.textSecondary
        )
    }
}

@Composable
private fun BankRateCard(
    rate: BankRateModel,
    isHighestBuy: Boolean,
    isLowestSell: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = when {
                    isHighestBuy -> CurrencyColors.success.copy(alpha = 0.4f)
                    isLowestSell -> CurrencyColors.button.copy(alpha = 0.4f)
                    else -> CurrencyColors.itemBackground
                },
                shape = RoundedCornerShape(10.dp)
            )
            .background(CurrencyColors.itemBackground)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = rate.bankName,
            style = CurrencyTypography.textMedium,
            color = CurrencyColors.text,
            modifier = Modifier.weight(1f).padding(end = 8.dp),
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
}

@Composable
private fun CurrencyChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val accent = CurrencyColors.button
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) accent else CurrencyColors.itemBackground)
            .border(
                width = 1.dp,
                color = if (selected) accent else CurrencyColors.itemBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = CurrencyTypography.textSemiBold,
            color = if (selected) Color.White else CurrencyColors.textSecondary
        )
    }
}