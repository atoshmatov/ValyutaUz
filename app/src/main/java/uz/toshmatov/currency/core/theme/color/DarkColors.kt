package uz.toshmatov.currency.core.theme.color

import androidx.compose.ui.graphics.Color

internal fun darkColors(accent: Color = BLUE): CurrencyColors = CurrencyColors(
    background = BLACK,
    text = WHITE,
    textSecondary = GRAY50,
    icon = accent,
    iconGray = GRAY40,
    error = RED60,
    success = GREEN,
    button = accent,
    bottomBar = DARK,
    bottomBarIcon = GRAY,
    bottomBarIconSelected = accent,
    bottomBarText = GRAY,
    bottomBarTextSelected = accent,
    bottomBarIndicator = BLUE200,
    itemBackground = GRAY1,
    shimmer = ShimmerDark,
    shimmerLight = MidnightMauve,
)
