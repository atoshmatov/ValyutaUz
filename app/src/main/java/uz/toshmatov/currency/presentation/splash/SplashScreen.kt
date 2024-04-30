package uz.toshmatov.currency.presentation.splash

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import uz.toshmatov.currency.presentation.main.MainScreen
import uz.toshmatov.currency.presentation.splash.component.AnimatedPreloader

class SplashScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val navigator = LocalNavigator.current
            Box {
                AnimatedPreloader(modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center))
            }

            LaunchedEffect(Unit) {
                delay(2000)
                navigator?.replace(MainScreen())
            }
        }
    }
}