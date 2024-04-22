package uz.toshmatov.currency.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import uz.toshmatov.currency.core.theme.color.CurrencyColors
import uz.toshmatov.currency.core.theme.color.LocalColors
import uz.toshmatov.currency.core.theme.deminsion.CurrencyDimensions
import uz.toshmatov.currency.core.theme.deminsion.LocalDimensions
import uz.toshmatov.currency.core.theme.typography.CurrencyTypography
import uz.toshmatov.currency.core.theme.typography.LocalTypography

val CurrencyColors
    @Composable
    get() = AppTheme.colors

val CurrencyDimensions
    @Composable
    get() = AppTheme.dimensions

val CurrencyTypography
    @Composable
    get() = AppTheme.typography

internal object AppTheme {
    val colors: CurrencyColors
        @Composable
        @ReadOnlyComposable
        get() = LocalColors.current

    val dimensions: CurrencyDimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalDimensions.current

    val typography: CurrencyTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
