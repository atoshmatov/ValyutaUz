package uz.toshmatov.currency.presentation.main.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyIcon
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CurrencyColors.background)
            .padding(vertical = 12.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = CurrencyColors.text,
            style = CurrencyTypography.textSemiBold
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.clickable { onClick() },
                text = string.home_all_item.resource,
                color = CurrencyColors.iconGray,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.width(2.dp))
            CurrencyIcon(
                image = drawable.ic_arrow_right,
                size = 20.dp,
                tint = CurrencyColors.iconGray,
                onClick = onClick,
            )
        }
    }
}