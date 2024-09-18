package uz.toshmatov.currency.core.theme.typography

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import uz.toshmatov.currency.core.utils.font

private val font_montserrat = FontFamily(
    Font(font.montserrat_black, FontWeight.Normal),
    Font(font.montserrat_bold, FontWeight.Bold),
    Font(font.montserrat_extra_bold, FontWeight.ExtraBold),
    Font(font.montserrat_extra_light, FontWeight.ExtraLight),
    Font(font.montserrat_light, FontWeight.Light),
    Font(font.montserrat_medium, FontWeight.Medium),
    Font(font.montserrat_regular, FontWeight.Normal),
    Font(font.montserrat_semi_bold, FontWeight.SemiBold),
    Font(font.montserrat_thin, FontWeight.Thin),
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
