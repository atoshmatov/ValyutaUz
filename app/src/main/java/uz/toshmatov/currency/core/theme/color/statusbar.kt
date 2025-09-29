package uz.toshmatov.currency.core.theme.color

import android.app.Activity
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat

@Composable
fun View.SetLightStatusBarAppearance(darkTheme: Boolean) {
    if (!this.isInEditMode) {
        val currentWindow = (this.context as? Activity)?.window
            ?: throw Exception("Not in an activity - unable to get Window reference")

        SideEffect {
            WindowCompat.getInsetsController(currentWindow, this).isAppearanceLightStatusBars =
                !darkTheme
            WindowCompat.getInsetsController(currentWindow, this).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }
}