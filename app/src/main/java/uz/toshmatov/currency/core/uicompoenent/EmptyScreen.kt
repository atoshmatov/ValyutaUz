package uz.toshmatov.currency.core.uicompoenent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.drawable

@Composable
fun EmptyScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CurrencyIcon(image = drawable.ic_empty_folder)
    }
}