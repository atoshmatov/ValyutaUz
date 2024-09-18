package uz.toshmatov.currency.core.uicompoenent

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

@SuppressLint("UnrememberedMutableInteractionSource")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyTextField(
    modifier: Modifier = Modifier,
    @StringRes placeHolderResId: Int = string.home_cbu,
    onValueChange: (String) -> Unit,
    onFocus: () -> Unit = {},
) {
    var text by rememberSaveable { mutableStateOf("") }
    val interactionSource = MutableInteractionSource()

    BasicTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(text)
        },
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CurrencyColors.bottomBar)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onFocus()
                }
            },
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = CurrencyColors.text,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
        ),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(CurrencyColors.text)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = innerTextField,
            placeholder = {
                Text(
                    text = placeHolderResId.resource,
                    color = CurrencyColors.textSecondary,
                    style = CurrencyTypography.textMedium,
                )
            },
            enabled = true,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedLeadingIconColor = CurrencyColors.icon,
                unfocusedLeadingIconColor = CurrencyColors.icon,
                focusedContainerColor = CurrencyColors.bottomBar,
                unfocusedContainerColor = CurrencyColors.bottomBar,
                focusedIndicatorColor = CurrencyColors.bottomBar,
                unfocusedIndicatorColor = CurrencyColors.bottomBar,
                cursorColor = CurrencyColors.text,
                focusedTextColor = CurrencyColors.text,
                unfocusedTextColor = CurrencyColors.text,
                disabledTextColor = CurrencyColors.textSecondary
            ),
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            leadingIcon = {
                CurrencyIcon(image = drawable.ic_search, size = 16.dp)
            },
            contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                top = CurrencyDimensions.empty,
                bottom = CurrencyDimensions.empty,
            ),
        )
    }
}