package uz.toshmatov.currency.presentation.main.tabscreen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: () -> Unit = {},
    isDataStale: Boolean = false
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(CurrencyColors.background)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {}
            )
            .padding(vertical = 12.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = CurrencyColors.button,
                style = CurrencyTypography.textSemiBold
            )
            if (isDataStale) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = string.home_stale_chip.resource,
                    color = CurrencyColors.error,
                    style = CurrencyTypography.captionUppercase,
                    modifier = Modifier
                        .background(
                            color = CurrencyColors.error.copy(alpha = if (isDarkTheme) 0.22f else 0.12f),
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        TextButton(
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() }
        ) {
            Text(
                text = string.home_all_item.resource,
                color = CurrencyColors.button,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(id = drawable.ic_arrow_right),
                contentDescription = "see all items",
                tint = CurrencyColors.button
            )
        }
    }
}
