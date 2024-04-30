package uz.toshmatov.currency.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.presentation.main.MainScreen
import uz.toshmatov.currency.presentation.splash.SplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyTheme {
                Navigator(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        MainScreen()
                    } else {
                        SplashScreen()
                    }
                ) {
                    SlideTransition(it)
                }
            }
        }
    }
}