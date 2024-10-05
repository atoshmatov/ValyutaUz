package uz.toshmatov.currency.presentation.splash

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.delay
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.raw
import uz.toshmatov.currency.presentation.main.MainScreen
import uz.toshmatov.currency.presentation.splash.component.AnimatedPreloader

class SplashScreen : AndroidScreen() {
    @SuppressLint("SuspiciousIndentation")
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CurrencyColors.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val navigator = LocalNavigator.current
                AnimatedPreloader(modifier = Modifier
                    .size(200.dp),
                    raw.currency_app_animation
                )
            LaunchedEffect(Unit) {
                delay(2000)
                navigator?.replace(MainScreen())
            }
        }
    }
}