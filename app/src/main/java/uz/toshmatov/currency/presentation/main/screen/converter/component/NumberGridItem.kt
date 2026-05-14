package uz.toshmatov.currency.presentation.main.screen.converter.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.luminance
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography

sealed interface NumberKeyboardAction {
    data class Digit(val value: String) : NumberKeyboardAction
    data object Decimal : NumberKeyboardAction
    data object Backspace : NumberKeyboardAction
    data object ClearAll : NumberKeyboardAction
}

private data class NumberKeyboardKey(
    val label: String,
    val action: NumberKeyboardAction,
    val longPressAction: NumberKeyboardAction? = null,
)

private val keyboardKeys = listOf(
    NumberKeyboardKey(label = "1", action = NumberKeyboardAction.Digit("1")),
    NumberKeyboardKey(label = "2", action = NumberKeyboardAction.Digit("2")),
    NumberKeyboardKey(label = "3", action = NumberKeyboardAction.Digit("3")),
    NumberKeyboardKey(label = "4", action = NumberKeyboardAction.Digit("4")),
    NumberKeyboardKey(label = "5", action = NumberKeyboardAction.Digit("5")),
    NumberKeyboardKey(label = "6", action = NumberKeyboardAction.Digit("6")),
    NumberKeyboardKey(label = "7", action = NumberKeyboardAction.Digit("7")),
    NumberKeyboardKey(label = "8", action = NumberKeyboardAction.Digit("8")),
    NumberKeyboardKey(label = "9", action = NumberKeyboardAction.Digit("9")),
    NumberKeyboardKey(label = ".", action = NumberKeyboardAction.Decimal),
    NumberKeyboardKey(label = "0", action = NumberKeyboardAction.Digit("0")),
    NumberKeyboardKey(
        label = "C",
        action = NumberKeyboardAction.Backspace,
        longPressAction = NumberKeyboardAction.ClearAll
    )
)

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun NumberGridItem(
    modifier: Modifier = Modifier,
    keyHeight: Dp = 80.dp,
    onAction: (NumberKeyboardAction) -> Unit
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    LazyVerticalStaggeredGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        columns = StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(CurrencyDimensions.extraSmall),
        verticalItemSpacing = CurrencyDimensions.extraSmall
    ) {
        items(12) {
            val keyboardKey = keyboardKeys[it]
            val shape = RoundedCornerShape(CurrencyDimensions.medium)
            Card(
                modifier = Modifier
                    .height(keyHeight)
                    .clip(shape)
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                        onClick = { onAction(keyboardKey.action) },
                        onLongClick = keyboardKey.longPressAction?.let { action ->
                            { onAction(action) }
                        }
                    ),
                shape = shape,
                border = BorderStroke(
                    width = if (isDarkTheme) 1.dp else 0.8.dp,
                    color = if (isDarkTheme) {
                        CurrencyColors.textSecondary.copy(alpha = 0.2f)
                    } else {
                        CurrencyColors.textSecondary.copy(alpha = 0.12f)
                    }
                ),
                colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = keyboardKey.label,
                        color = CurrencyColors.text,
                        style = CurrencyTypography.buttonCalculator
                    )
                }
            }
        }
    }
}
