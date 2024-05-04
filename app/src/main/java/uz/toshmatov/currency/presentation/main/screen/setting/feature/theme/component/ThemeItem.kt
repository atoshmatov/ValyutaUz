package uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.data.local.model.ThemeMode

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    onClick: (ThemeMode) -> Unit,
    themeModel: ThemeModel,
    isCheck: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CurrencyColors.bottomBar)
            .clickable {
                onClick(themeModel.themeMode)
            }
            .padding(16.dp),
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp),
            painter = painterResource(id = themeModel.icon),
            contentDescription = themeModel.title.resource,
            tint = CurrencyColors.icon
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp),
            text = themeModel.title.resource,
            style = CurrencyTypography.textMedium,
            color = CurrencyColors.text,
            maxLines = 1,
        )
        if (isCheck)
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd),
                painter = painterResource(id = drawable.ic_check),
                contentDescription = themeModel.title.resource,
                tint = CurrencyColors.icon
            )
    }
}