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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.extensions.getBankLogo
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.domain.model.ExchangeModel

@Composable
fun CurrencyItems(
    modifier: Modifier = Modifier,
    exchangeModel: ExchangeModel,
    cbu: String
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
        Image(
            modifier = Modifier
                .size(36.dp),
            painter = painterResource(id = getBankLogo(exchangeModel.bank)),
            contentDescription = exchangeModel.bank
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = exchangeModel.bank,
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
                    contentDescription = exchangeModel.bank,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = exchangeModel.buy,
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
                    contentDescription = exchangeModel.bank,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = cbu,
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
                    contentDescription = exchangeModel.bank,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = exchangeModel.sell,
                color = CurrencyColors.textSecondary,
                style = CurrencyTypography.captionUppercase
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(
                    painter = painterResource(id = drawable.ic_update),
                    contentDescription = exchangeModel.bank,
                    tint = CurrencyColors.icon,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = exchangeModel.date,
                    color = CurrencyColors.textSecondary,
                    style = CurrencyTypography.captionUppercase
                )
            }
        }
    }
}