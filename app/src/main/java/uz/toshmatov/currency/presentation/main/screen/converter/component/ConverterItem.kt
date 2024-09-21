package uz.toshmatov.currency.presentation.main.screen.converter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyIcon
import uz.toshmatov.currency.core.utils.drawable

@Composable
fun ConverterItem(
    modifier: Modifier = Modifier,
    number: String = "0",
    codeName: String = "so'm",
    rate: String,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(CurrencyDimensions.small))
            .background(CurrencyColors.bottomBar)
            .padding(CurrencyDimensions.medium),
    ) {
        Text(
            modifier = Modifier.align(Alignment.TopStart),
            text = "1 $codeName = $rate",
            style = CurrencyTypography.textSemiBold,
            color = CurrencyColors.textSecondary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(top = CurrencyDimensions.medium)
        ) {
            Text(
                text = number,
                color = CurrencyColors.text,
                style = CurrencyTypography.enterAmountSecondary,
            )
            Spacer(modifier = Modifier.weight(1f))
            CurrencyIcon(
                image = drawable.ic_arrow_down,
                tint = CurrencyColors.success,
                onClick = {},
                contentDescription = "arrow down"
            )
        }
    }
}