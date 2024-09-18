package uz.toshmatov.currency.core.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import uz.toshmatov.currency.core.extensions.StatusBarStyle
import uz.toshmatov.currency.core.extensions.applyTheme
import uz.toshmatov.currency.core.theme.color.CurrencyColors
import uz.toshmatov.currency.core.theme.color.LocalColors
import uz.toshmatov.currency.core.theme.color.darkColors
import uz.toshmatov.currency.core.theme.color.lightColors
import uz.toshmatov.currency.core.theme.deminsion.CurrencyDimensions
import uz.toshmatov.currency.core.theme.deminsion.LocalDimensions
import uz.toshmatov.currency.core.theme.typography.CurrencyTypography
import uz.toshmatov.currency.core.theme.typography.LocalTypography

@Composable
fun CurrencyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    typography: CurrencyTypography = CurrencyTypography,
    dimensions: CurrencyDimensions = CurrencyDimensions,
    lightStatusBar: Boolean = !darkTheme,
    content: @Composable () -> Unit
) {

    val systemUiController = rememberSystemUiController()

    val colors: CurrencyColors = if (darkTheme) darkColors() else lightColors()

    val rippleIndication = rememberRipple()

    val rememberedColors = remember { colors.copy() }.apply { updateColorsFrom(colors) }

    StatusBarStyle(
        statusBarColor = colors.background,
        navigationBarColor = colors.background
    )

    SideEffect {
        systemUiController.applyTheme(darkTheme && !lightStatusBar)
    }

    CompositionLocalProvider(
        LocalColors provides rememberedColors,
        LocalDimensions provides dimensions,
        LocalTypography provides typography,
        LocalIndication provides rippleIndication
    ) {
        MaterialTheme {
            content()
        }
    }
}