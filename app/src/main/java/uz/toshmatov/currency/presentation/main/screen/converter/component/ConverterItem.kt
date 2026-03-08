package uz.toshmatov.currency.presentation.main.screen.converter.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable

@Composable
fun ConverterItem(
    modifier: Modifier = Modifier,
    title: String = "",
    number: String = "0",
    codeName: String = "so'm",
    rate: String,
    onNumberTap: ((Int) -> Unit)? = null,
    onRateTap: (() -> Unit)? = null,
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    var textLayoutResult by remember(number) { mutableStateOf<TextLayoutResult?>(null) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(CurrencyDimensions.medium),
        border = BorderStroke(
            width = if (isDarkTheme) 1.dp else 0.7.dp,
            color = if (isDarkTheme) {
                CurrencyColors.textSecondary.copy(alpha = 0.2f)
            } else {
                CurrencyColors.textSecondary.copy(alpha = 0.16f)
            }
        ),
        colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
    ) {
        Column(
            modifier = Modifier.padding(CurrencyDimensions.medium),
            horizontalAlignment = Alignment.Start,
        ) {
            if (title.isNotBlank()) {
                Text(
                    text = title,
                    style = CurrencyTypography.captionRegular,
                    color = CurrencyColors.textSecondary
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(CurrencyDimensions.small))
                    .then(
                        if (onRateTap == null) {
                            Modifier
                        } else {
                            Modifier.clickable(onClick = onRateTap)
                        }
                    )
                    .padding(
                        horizontal = CurrencyDimensions.extraSmall,
                        vertical = 4.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "1 $codeName = $rate",
                    style = CurrencyTypography.textSemiBold,
                    color = if (onRateTap == null) {
                        CurrencyColors.textSecondary
                    } else {
                        CurrencyColors.button
                    }
                )
                if (onRateTap != null) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = drawable.ic_arrow_down),
                        contentDescription = null,
                        tint = CurrencyColors.button
                    )
                }
            }
            AnimatedContent(
                targetState = number,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 140))
                        .togetherWith(fadeOut(animationSpec = tween(durationMillis = 100)))
                },
                label = "converter_amount_content"
            ) { animatedNumber ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = CurrencyDimensions.medium)
                        .then(
                            if (onNumberTap == null) {
                                Modifier
                            } else {
                                Modifier.pointerInput(animatedNumber) {
                                    detectTapGestures { tapOffset ->
                                        val offset = textLayoutResult
                                            ?.getOffsetForPosition(tapOffset)
                                            ?.coerceIn(0, animatedNumber.length)
                                            ?: animatedNumber.length
                                        onNumberTap(offset)
                                    }
                                }
                            }
                        ),
                    text = animatedNumber,
                    color = CurrencyColors.text,
                    style = CurrencyTypography.enterAmountSecondary.copy(
                        fontSize = 28.sp,
                        lineHeight = 40.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = { textLayoutResult = it }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ConvertItemPreview() = CurrencyTheme {
    ConverterItem(
        title = "You send",
        number = "1 200 333 333",
        codeName = "so'm",
        rate = "12 300 so'm"
    )
}
