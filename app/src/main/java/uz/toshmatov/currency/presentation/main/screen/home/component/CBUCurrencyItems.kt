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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
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
import uz.toshmatov.currency.domain.model.CBUModel

@Composable
fun CurrencyItems(
    modifier: Modifier = Modifier,
    cbuModel: CBUModel,
) {

    val priceIcon by remember {
        derivedStateOf {
            if (cbuModel.diff.contains("-")) {
                mutableIntStateOf(drawable.ic_money_recive)
            } else {
                mutableIntStateOf(drawable.ic_money_send)
            }
        }
    }
    val priceColor by remember {
        derivedStateOf {
            cbuModel.diff.contains("-")
        }
    }

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
            //model = CurrencyCode.valueOf(cbuModel.ccy).flag,
            model = ImageRequest.Builder(LocalContext.current)
                .data(CurrencyCode.valueOf(cbuModel.ccy).flag).crossfade(true).build(),
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
                text = cbuModel.ccy,
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = cbuModel.ccyNmUZ,
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
                text = cbuModel.rate + " so'm",
                color = CurrencyColors.text,
                style = CurrencyTypography.buttonRegular
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cbuModel.diff,
                    color = if (priceColor) CurrencyColors.error else CurrencyColors.success,
                    style = CurrencyTypography.captionUppercase
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    painter = painterResource(id = priceIcon.intValue),
                    contentDescription = "Price Icon",
                    tint = if (priceColor) CurrencyColors.error else CurrencyColors.success,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}