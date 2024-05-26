package uz.toshmatov.currency.core.extensions

import com.google.accompanist.systemuicontroller.SystemUiController
import uz.toshmatov.currency.core.theme.color.BLACK
import uz.toshmatov.currency.core.theme.color.DARK
import uz.toshmatov.currency.core.theme.color.GRAY10
import uz.toshmatov.currency.core.theme.color.WHITE

internal fun SystemUiController.applyTheme(isDarkTheme: Boolean) {
    val statusBarColor = if (isDarkTheme) BLACK else GRAY10
    val navigationBarColor = if (isDarkTheme) DARK else WHITE

    setSystemBarsColor(
        color = statusBarColor,
    )
    setNavigationBarColor(
        color = navigationBarColor,
    )
}
