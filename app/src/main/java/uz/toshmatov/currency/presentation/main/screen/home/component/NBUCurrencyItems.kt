package uz.toshmatov.currency.presentation.main.screen.home.component

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import uz.toshmatov.currency.core.extensions.CurrencyCode
import uz.toshmatov.currency.core.extensions.toNumber
import uz.toshmatov.currency.core.extensions.toSom
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.domain.model.NBUModel

@Composable
fun NBUCurrencyItems(
    modifier: Modifier = Modifier,
    nbuModel: NBUModel,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = CurrencyDimensions.medium,
                vertical = CurrencyDimensions.extraSmall
            )
            .clip(RoundedCornerShape(CurrencyDimensions.small))
            .background(CurrencyColors.bottomBar)
            .clickable { }
            .padding(CurrencyDimensions.itemSpace),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(36.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(CurrencyCode.valueOf(nbuModel.code).flag).crossfade(true).build(),
            contentDescription = nbuModel.code,
            placeholder = painterResource(id = drawable.ic_empty_flag)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nbuModel.title + " " + nbuModel.code,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = CurrencyColors.text,
                style = CurrencyTypography.textSemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = string.home_buy_price.resource,
                    color = CurrencyColors.text,
                    style = CurrencyTypography.textSemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = drawable.ic_money_recive),
                    contentDescription = nbuModel.code,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = nbuModel.nbuBuyPrice.toNumber().toSom(),
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionUppercase
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = string.home_update_date.resource,
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionUppercase
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = drawable.ic_cbu_logo),
                    contentDescription = nbuModel.code,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nbuModel.cbPrice.toNumber().toSom(),
                    color = CurrencyColors.icon,
                    style = CurrencyTypography.buttonRegular
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = string.home_sell_price.resource,
                    color = CurrencyColors.text,
                    style = CurrencyTypography.textSemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(id = drawable.ic_money_send),
                    contentDescription = nbuModel.code,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = nbuModel.nbuCellPrice.toNumber().toSom(),
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionUppercase
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_update),
                    contentDescription = nbuModel.code,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = nbuModel.date,
                    color = CurrencyColors.textSecondary,
                    style = CurrencyTypography.captionUppercase
                )
            }
        }
    }
}
