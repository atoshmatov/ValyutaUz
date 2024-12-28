package spacer

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.Space(
    height: Dp = 0.dp,
    width: Dp = 0.dp,
    weight: Float = 0f,
) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
            .then(if (weight > 0f) Modifier.weight(weight) else Modifier),
    )
}

@Composable
fun RowScope.Space(
    height: Dp = 0.dp,
    width: Dp = 0.dp,
    weight: Float = 0f,
) {
    Spacer(
        modifier = Modifier
            .height(height)
            .width(width)
            .then(if (weight > 0f) Modifier.weight(weight) else Modifier),
    )
}

@Composable
fun LazyListScope.Space(space: Dp = 0.dp) {
    Spacer(modifier = Modifier.height(space))
}
