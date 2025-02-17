package uz.toshmatov.currency.presentation.empty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable

@Composable
fun EmptyScreen(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.itemBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            modifier = Modifier
                .size(200.dp),
            model = drawable.ic_empty_state,
            contentDescription = "empty_state",
        )

        Text(
            modifier = Modifier.padding(horizontal = 12.dp),
            text = text,
            color = CurrencyColors.button,
            style = CurrencyTypography.textSemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun App() = CurrencyTheme {
    EmptyScreen(text = "text")
}