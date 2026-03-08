package uz.toshmatov.currency.presentation.main.tabscreen.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uz.toshmatov.currency.core.extensions.addPlus
import uz.toshmatov.currency.core.image.imageToRequest
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.domain.model.CBUModel

@Composable
fun CBUCurrencyItems(
    modifier: Modifier = Modifier,
    cbuModel: CBUModel,
    isDataStale: Boolean = false,
    cbuItemClick: () -> Unit
) {
    val priceIcon by remember {
        derivedStateOf {
            if (cbuModel.diff.contains("-")) {
                mutableIntStateOf(drawable.ic_chart_down)
            } else {
                mutableIntStateOf(drawable.ic_chart_up)
            }
        }
    }
    val priceColor by remember {
        derivedStateOf {
            cbuModel.diff.contains("-")
        }
    }
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.985f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "home_card_press_scale"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = CurrencyDimensions.medium,
                vertical = CurrencyDimensions.extraSmall
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        onClick = cbuItemClick,
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
        colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
    ) {
        Row(
            modifier = Modifier.padding(CurrencyDimensions.itemSpace),
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
                        .size(24.dp),
                    model = cbuModel.ccy.imageToRequest(),
                    contentDescription = cbuModel.ccy,
                    placeholder = painterResource(id = drawable.ic_empty_flag)
                )
            }
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
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = cbuModel.ccyName,
                    color = CurrencyColors.textSecondary,
                    style = CurrencyTypography.captionUppercase
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = cbuModel.rate,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(160))
                            .togetherWith(fadeOut(animationSpec = tween(120)))
                    },
                    label = "home_rate_animation"
                ) { animatedRate ->
                    Text(
                        text = animatedRate,
                        color = CurrencyColors.text,
                        style = CurrencyTypography.labelSemiBold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedContent(
                        targetState = cbuModel.diff,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(160))
                                .togetherWith(fadeOut(animationSpec = tween(120)))
                        },
                        label = "home_diff_animation"
                    ) { animatedDiff ->
                        Text(
                            text = if (priceColor) animatedDiff else animatedDiff.addPlus(),
                            color = if (priceColor) CurrencyColors.error else CurrencyColors.success,
                            style = CurrencyTypography.captionUppercase
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        painter = painterResource(id = priceIcon.intValue),
                        contentDescription = cbuModel.ccy,
                        tint = if (priceColor) CurrencyColors.error else CurrencyColors.success,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (isDataStale) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = string.home_stale_chip.resource,
                        color = CurrencyColors.error,
                        style = CurrencyTypography.captionUppercase
                    )
                }
            }
        }
    }
}
