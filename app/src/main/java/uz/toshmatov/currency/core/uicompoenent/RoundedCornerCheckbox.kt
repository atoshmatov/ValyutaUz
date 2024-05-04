package uz.toshmatov.currency.core.uicompoenent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.core.theme.ThemedPreview
import uz.toshmatov.currency.core.utils.drawable

@Composable
fun RoundedCornerCheckbox(
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val imageVector = if (checked) drawable.ic_circle else drawable.ic_un_checkbox
    val color = if (checked) CurrencyColors.icon else Color.Transparent
    val tint = if (checked) Color.White else CurrencyColors.iconGray

    Icon(
        painter = painterResource(id = imageVector),
        contentDescription = null,
        tint = tint,
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color = color)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onCheckedChange(!checked) },
            ),
    )
}

@ThemedPreview
@Composable
private fun CircularCheckboxPreview() {
    CurrencyTheme {
        Column {
            RoundedCornerCheckbox(onCheckedChange = { }, checked = true)
            RoundedCornerCheckbox(onCheckedChange = { }, checked = false)
        }
    }
}