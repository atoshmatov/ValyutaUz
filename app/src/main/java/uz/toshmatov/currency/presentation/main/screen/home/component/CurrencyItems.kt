package uz.toshmatov.currency.presentation.main.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uz.toshmatov.currency.core.extensions.CurrencyCode
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.domain.model.ExchangeModel

@Composable
fun CurrencyItems(
    modifier: Modifier = Modifier,
    nbuModel: ExchangeModel,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = CurrencyDimensions.medium,
                vertical = CurrencyDimensions.extraSmall
            )
            .clip(RoundedCornerShape(CurrencyDimensions.small))
            .background(CurrencyColors.itemBackground)
            .clickable { }
            .padding(CurrencyDimensions.itemSpace),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(32.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(CurrencyCode.valueOf("USD").flag).crossfade(true).build(),
            contentDescription = "This is an example image",
            placeholder = painterResource(id = drawable.ic_empty_flag)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nbuModel.bank,
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nbuModel.date,
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionUppercase
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nbuModel.sell + " so'm",
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = nbuModel.buy + " so'm",
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
        }
    }
}