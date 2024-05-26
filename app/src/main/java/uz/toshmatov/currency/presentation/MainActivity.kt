package uz.toshmatov.currency.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import dagger.hilt.android.AndroidEntryPoint
import uz.toshmatov.currency.core.extensions.getCurrentThemeMode
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.presentation.main.MainScreen
import uz.toshmatov.currency.presentation.splash.SplashScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    //private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //connectivityObserver = NetworkConnectivityObserver(applicationContext)

        setContent {
            val themeMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            CurrencyTheme(
                darkTheme = getCurrentThemeMode(themeMode),
            ) {
                /*val status by connectivityObserver.observe().collectAsState(
                    initial = ConnectivityObserver.Status.UNAVAILABLE
                )
                if (status == ConnectivityObserver.Status.UNAVAILABLE || status == ConnectivityObserver.Status.LOST || status == ConnectivityObserver.Status.LOSING) {
                    NetworkError()
                }*/
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