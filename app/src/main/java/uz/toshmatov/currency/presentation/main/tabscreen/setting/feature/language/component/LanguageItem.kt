package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.language.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yariksoffice.lingver.Lingver
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource

@Composable
fun LanguageItem(
    modifier: Modifier = Modifier,
    languageModel: LanguageModel,
    onLanguageSelected: (String) -> Unit,
) {
    val currentLanguageCode = Lingver.getInstance().getLocale().language
    val isSelected = languageModel.code == currentLanguageCode
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "language_item_press_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = CurrencyDimensions.extraSmall)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = { onLanguageSelected(languageModel.code) },
        interactionSource = interactionSource,
        shape = RoundedCornerShape(CurrencyDimensions.medium),
        border = when {
            isSelected -> BorderStroke(1.dp, CurrencyColors.button)
            isDarkTheme -> BorderStroke(
                1.dp,
                CurrencyColors.textSecondary.copy(alpha = 0.15f)
            )
            else -> BorderStroke(
                0.8.dp,
                CurrencyColors.textSecondary.copy(alpha = 0.12f)
            )
        },
        colors = CardDefaults.cardColors(
            containerColor = CurrencyColors.bottomBar
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = CurrencyDimensions.medium,
                vertical = 12.dp
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
                AsyncImage(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .size(22.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(languageModel.icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = languageModel.title.resource,
                    placeholder = painterResource(id = drawable.ic_empty_flag)
                )
            }
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium),
                text = languageModel.title.resource,
                color = CurrencyColors.text,
                style = CurrencyTypography.labelSemiMedium
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(animationSpec = tween(160)) + scaleIn(animationSpec = tween(160)),
                exit = fadeOut(animationSpec = tween(120)) + scaleOut(animationSpec = tween(120))
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(id = drawable.ic_active),
                    contentDescription = languageModel.title.resource,
                    tint = CurrencyColors.button
                )
            }
        }
    }
}
