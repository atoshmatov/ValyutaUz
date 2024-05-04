package uz.toshmatov.currency.presentation.main.screen.setting.component

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
import uz.toshmatov.currency.presentation.main.screen.setting.model.ActionType
import uz.toshmatov.currency.presentation.main.screen.setting.model.SettingModel

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    settingModel: SettingModel,
    onClick: (ActionType) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(CurrencyColors.bottomBar)
            .clickable {
                onClick(settingModel.actionType)
            }
            .padding(16.dp),
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(24.dp),
            painter = painterResource(id = settingModel.icon),
            contentDescription = settingModel.title.resource,
            tint = CurrencyColors.icon
        )
        Text(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 32.dp),
            text = settingModel.title.resource,
            color = CurrencyColors.text,
            style = CurrencyTypography.textMedium
        )
        if (!settingModel.isActivated) {
            Icon(
                modifier = Modifier.align(Alignment.CenterEnd),
                painter = painterResource(id = drawable.ic_right),
                contentDescription = settingModel.title.resource,
                tint = CurrencyColors.iconGray
            )
        }
    }
}