package uz.toshmatov.currency.presentation.main.tabscreen.setting.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.presentation.main.tabscreen.setting.model.ActionType

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    settingModel: uz.toshmatov.currency.presentation.main.tabscreen.setting.model.SettingModel,
    onClick: (ActionType) -> Unit
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "setting_item_press_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = { onClick(settingModel.actionType) },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(CurrencyDimensions.medium),
        border = BorderStroke(
            width = if (isDarkTheme) 1.dp else 0.8.dp,
            color = if (isDarkTheme) {
                CurrencyColors.textSecondary.copy(alpha = 0.15f)
            } else {
                CurrencyColors.textSecondary.copy(alpha = 0.12f)
            }
        ),
        colors = CardDefaults.cardColors(
            containerColor = CurrencyColors.bottomBar
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CurrencyDimensions.medium,
                vertical = 14.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = CurrencyColors.button.copy(alpha = if (isDarkTheme) 0.24f else 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = settingModel.icon),
                    contentDescription = settingModel.title.resource,
                    tint = CurrencyColors.button,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium),
                text = settingModel.title.resource,
                color = CurrencyColors.text,
                style = CurrencyTypography.labelSemiMedium
            )
            if (!settingModel.isActivated) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = drawable.ic_right),
                    contentDescription = settingModel.title.resource,
                    tint = CurrencyColors.textSecondary
                )
            }
        }
    }
}
