package uz.toshmatov.currency.core.extensions

import com.google.accompanist.systemuicontroller.SystemUiController
import uz.toshmatov.currency.core.theme.color.GRAY100
import uz.toshmatov.currency.core.theme.color.WHITE

internal fun SystemUiController.applyTheme(isDarkTheme: Boolean) {
    val statusBarColor = if (isDarkTheme) GRAY100 else WHITE

    setSystemBarsColor(
        color = statusBarColor,
    )
}
