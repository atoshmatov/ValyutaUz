package uz.toshmatov.currency.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import uz.toshmatov.currency.core.theme.color.CurrencyColors
import uz.toshmatov.currency.core.theme.color.LocalColors
import uz.toshmatov.currency.core.theme.color.SetLightStatusBarAppearance
import uz.toshmatov.currency.core.theme.color.darkColors
import uz.toshmatov.currency.core.theme.color.lightColors
import uz.toshmatov.currency.core.theme.deminsion.CurrencyDimensions
import uz.toshmatov.currency.core.theme.deminsion.LocalDimensions
import uz.toshmatov.currency.core.theme.typography.CurrencyTypography
import uz.toshmatov.currency.core.theme.typography.LocalTypography
import uz.toshmatov.currency.data.local.model.AccentColor

@Composable
fun CurrencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    accentColor: AccentColor = AccentColor.Blue,
    typography: CurrencyTypography = CurrencyTypography,
    dimensions: CurrencyDimensions = CurrencyDimensions,
    content: @Composable () -> Unit
) {
    val accent: Color = accentColor.color
    val colors: CurrencyColors = if (darkTheme) darkColors(accent) else lightColors(accent)
    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }

    val view = LocalView.current
    view.SetLightStatusBarAppearance(darkTheme)

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography
    ) {
        MaterialTheme {
            content()
        }
    }
}
