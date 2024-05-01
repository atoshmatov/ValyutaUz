package uz.toshmatov.currency.presentation.main.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(CurrencyColors.background)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = CurrencyColors.text,
            style = CurrencyTypography.textSemiBold
        )
    }
}