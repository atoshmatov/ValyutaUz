package uz.toshmatov.currency.core.uicompoenent.shimmer

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions

const val SHIMMED_ITEMS_NUMBER = 6

@Composable
fun ShimmedList(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    shimmerItemCount: Int = SHIMMED_ITEMS_NUMBER,
) {
    Column(
        modifier = modifier
            .padding(paddingValues)
            .background(CurrencyColors.background),
    ) {
        repeat(shimmerItemCount) {
            ShimmedItem()
        }
    }
}

@Composable
fun ShimmedItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(CurrencyDimensions.extraLarge)
            .background(CurrencyColors.background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(CurrencyDimensions.small))
                .shimmerEffect(),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .fillMaxWidth(0.7f)
                    .height(12.dp)
                    .shimmerEffect(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .fillMaxWidth(0.5f)
                    .height(8.dp)
                    .shimmerEffect(),
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
        ),
        label = "",
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                CurrencyColors.shimmer,
                CurrencyColors.shimmerLight,
                CurrencyColors.shimmer,
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat()),
        ),
    ).onGloballyPositioned {
        size = it.size
    }
}
