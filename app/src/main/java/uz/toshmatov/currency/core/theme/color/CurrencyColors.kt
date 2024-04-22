package uz.toshmatov.currency.core.theme.color

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CurrencyColors(
    background: Color,
    text: Color,
    icon: Color,
    error: Color,
    button: Color
) {

    var background by mutableStateOf(background)
        private set

    var text by mutableStateOf(text)
        private set

    var icon by mutableStateOf(icon)
        private set

    var error by mutableStateOf(error)
        private set

    var button by mutableStateOf(button)
        private set

    fun copy(
        background: Color = this.background,
        text: Color = this.text,
        icon: Color = this.icon,
        error: Color = this.error,
        button: Color = this.button
    ) = CurrencyColors(
        background = background,
        text = text,
        icon = icon,
        error = error,
        button = button
    )

    fun updateColorsFrom(other: CurrencyColors) {
        background = other.background
        text = other.text
        icon = other.icon
        error = other.error
        button = other.button
    }
}