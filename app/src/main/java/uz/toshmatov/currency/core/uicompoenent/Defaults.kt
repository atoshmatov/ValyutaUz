@file:OptIn(ExperimentalMaterial3Api::class)

package uz.toshmatov.currency.core.uicompoenent

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource

@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    color: Color = Color.Transparent,
    contentDescription: String,
) {
    TopAppBar(
        modifier = modifier.padding(start = CurrencyDimensions.medium),
        title = {
            Text(
                text = title,
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.button,
                modifier = Modifier.padding(start = CurrencyDimensions.small),
            )
        },
        navigationIcon = {
            CurrencyIcon(
                image = drawable.ic_arrow_left,
                tint = CurrencyColors.button,
                onClick = onBackClick,
                contentDescription = contentDescription,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(color),
    )
}

@Composable
fun TopBar(
    @StringRes titleId: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    color: Color = Color.Transparent,
    contentDescription: String,
) {
    TopAppBar(
        modifier = modifier
            .padding(start = CurrencyDimensions.medium),
        title = {
            Text(
                text = titleId.resource,
                style = CurrencyTypography.textSemiBold,
                color = CurrencyColors.button,
                modifier = Modifier.padding(start = CurrencyDimensions.small),
            )
        },
        navigationIcon = {
            CurrencyIcon(
                image = drawable.ic_arrow_left,
                tint = CurrencyColors.button,
                onClick = onBackClick,
                contentDescription = contentDescription,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(color),
    )
}
