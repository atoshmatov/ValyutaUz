package uz.toshmatov.currency.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.toArgb(),
                darkScrim = Color.Transparent.toArgb(),
            )
        )
        setContent {
            val themeMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
            val launchConverter = parseConverterLaunch(intent)
            CurrencyTheme(
                darkTheme = getCurrentThemeMode(themeMode),
            ) {
                Navigator(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        MainScreen(
                            initialConverterCodeName = launchConverter?.codeName,
                            initialConverterCode = launchConverter?.code,
                            initialConverterRate = launchConverter?.rate
                        )
                    } else {
                        SplashScreen()
                    },
                ) {
                    SlideTransition(it)
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1002
            )
        }
    }

    private fun parseConverterLaunch(intent: Intent?): ConverterLaunchData? {
        val sourceIntent = intent ?: return null
        val shouldOpenConverter = sourceIntent.getBooleanExtra(EXTRA_OPEN_CONVERTER, false)
        if (!shouldOpenConverter) return null

        val code = sourceIntent.getStringExtra(EXTRA_CONVERTER_CODE).orEmpty().trim()
        val rate = sourceIntent.getStringExtra(EXTRA_CONVERTER_RATE).orEmpty().trim()
        if (code.isBlank() || rate.isBlank()) return null

        val codeName = sourceIntent.getStringExtra(EXTRA_CONVERTER_CODE_NAME)
            .orEmpty()
            .trim()
            .ifBlank { code }

        return ConverterLaunchData(
            codeName = codeName,
            code = code,
            rate = rate
        )
    }

    data class ConverterLaunchData(
        val codeName: String,
        val code: String,
        val rate: String
    )

    companion object {
        const val EXTRA_OPEN_CONVERTER = "extra_open_converter"
        const val EXTRA_CONVERTER_CODE_NAME = "extra_converter_code_name"
        const val EXTRA_CONVERTER_CODE = "extra_converter_code"
        const val EXTRA_CONVERTER_RATE = "extra_converter_rate"
    }
}
