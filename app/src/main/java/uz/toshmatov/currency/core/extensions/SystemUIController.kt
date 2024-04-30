package uz.toshmatov.currency.core.extensions

import com.google.accompanist.systemuicontroller.SystemUiController
import uz.toshmatov.currency.core.theme.color.BLACK
import uz.toshmatov.currency.core.theme.color.GRAY10

internal fun SystemUiController.applyTheme(isDarkTheme: Boolean) {
    val statusBarColor = if (isDarkTheme) BLACK else GRAY10

    setSystemBarsColor(
        color = statusBarColor,
    )
}
