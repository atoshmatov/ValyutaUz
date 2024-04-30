package uz.toshmatov.currency.core.theme.color

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Main Color
 * [Color]
 * @return c [Color]
 * */

internal val WHITE = Color(0xFFFFFFFF)
internal val BLACK = Color(0xFF000000)
internal val DARK = Color(0xFF1A1A1A)
internal val GREEN = Color(0xFF00C853)

//RED color
internal val RED10 = Color(0xFFFFEDED)
internal val RED60 = Color(0xFFCE2D2D)
internal val RED70 = Color(0xFF8A0C0C)

//GRAY
internal val GRAY = Color(0xFF727373)
internal val GRAY1 = Color(0xFF404040)
internal val GRAY10 = Color(0xFFF4F4F4)
internal val GRAY20 = Color(0xFFDEDEDE)
internal val GRAY30 = Color(0xFFC7C7C7)
internal val GRAY40 = Color(0xFFACACAC)
internal val GRAY50 = Color(0xFF8A8A8A)
internal val GRAY60 = Color(0xFF6A6A6A)
internal val GRAY70 = Color(0xFF535353)
internal val GRAY80 = Color(0xFF3F3F3F)
internal val GRAY90 = Color(0xFF303030)
internal val GRAY100 = Color(0xFF212121)

//BLUE
internal val BLUE = Color(0xFF4489F7)
internal val BLUE10 = Color(0xFFF0F5FC)
internal val BLUE20 = Color(0xFFCFE0FC)
internal val BLUE30 = Color(0xFFACCBFC)
internal val BLUE40 = Color(0xFF84B1FA)
internal val BLUE50 = Color(0xFF5691F0)
internal val BLUE60 = Color(0xFF4177CE)
internal val BLUE70 = Color(0xFF1D5BBF)
internal val BLUE80 = Color(0xFF114599)
internal val BLUE90 = Color(0xFF103570)
internal val BLUE100 = Color(0xFF15233B)
internal val BLUE200 = Color(0xFF1E2530)

val LocalColors = staticCompositionLocalOf { lightColors() }



