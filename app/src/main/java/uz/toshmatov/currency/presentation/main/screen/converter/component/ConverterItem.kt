package uz.toshmatov.currency.presentation.main.screen.converter.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTheme
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(CurrencyDimensions.small))
            .background(CurrencyColors.bottomBar)
            .padding(CurrencyDimensions.medium),
        horizontalAlignment = Alignment.Start,

    ) {
        Text(
            modifier = Modifier,
            text = "1 $codeName = $rate",
            style = CurrencyTypography.textSemiBold,
            color = CurrencyColors.textSecondary
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = CurrencyDimensions.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = number,
                color = CurrencyColors.text,
                style = CurrencyTypography.enterAmountSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            CurrencyIcon(
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                image = drawable.ic_arrow_down,
                tint = CurrencyColors.success,
                onClick = {},
                contentDescription = "arrow down"
            )
        }
    }
}

@Preview
@Composable
private fun ConvertItemPreview() = CurrencyTheme {
    ConverterItem(
        number = "1 200 333 333",
        codeName = "so'm",
        rate = "12 300 so'm"
    )
}