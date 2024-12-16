package uz.toshmatov.currency.core.theme.typography

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.toshmatov.currency.core.utils.font

private val font_montserrat = FontFamily(
    Font(
        resId = font.montserrat_black,
        weight = FontWeight.Normal
    ),
    Font(
        resId = font.montserrat_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resId = font.montserrat_extra_bold,
        weight = FontWeight.ExtraBold
    ),
    Font(
        resId = font.montserrat_extra_light,
        weight = FontWeight.ExtraLight
    ),
    Font(
        resId = font.montserrat_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = font.montserrat_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resId = font.montserrat_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = font.montserrat_semi_bold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resId = font.montserrat_thin,
        weight = FontWeight.Thin
    ),
)

data class CurrencyTypography(
    val enterAmountPrimary: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 64.sp,
        lineHeight = 80.sp,
    ),
    val enterAmountSecondary: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 32.sp,
        lineHeight = 48.sp,
    ),
    val buttonCalculator: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = font_montserrat,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    val buttonRegular: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = font_montserrat,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),
    val labelSemiBold: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = font_montserrat,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val labelSemiMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    val textSemiBold: TextStyle = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontFamily = font_montserrat,
        fontSize = 14.sp,
        lineHeight = 24.sp
    ),
    val textMedium: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 14.sp,
        lineHeight = 24.sp
    ),
    val captionUppercase: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 14.sp,
        lineHeight = 24.sp
    ),
    val captionRegular: TextStyle = TextStyle(
        fontWeight = FontWeight.Medium,
        fontFamily = font_montserrat,
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
)

internal val LocalTypography = staticCompositionLocalOf { CurrencyTypography() }
