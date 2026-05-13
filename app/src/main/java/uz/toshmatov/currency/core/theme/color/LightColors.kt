package uz.toshmatov.currency.core.theme.color

import androidx.compose.ui.graphics.Color

internal fun lightColors(accent: Color = BLUE): CurrencyColors = CurrencyColors(
    background = GRAY10,
    text = BLACK,
    textSecondary = GRAY50,
    icon = accent,
    iconGray = GRAY40,
    error = RED60,
    success = GREEN,
    button = accent,
    bottomBar = WHITE,
    bottomBarIcon = GRAY,
    bottomBarIconSelected = accent,
    bottomBarText = GRAY,
    bottomBarTextSelected = accent,
    bottomBarIndicator = WHITE,
    itemBackground = WHITE,
    shimmer = ShimmerLight,
    shimmerLight = ShimmerLineLight,
)
