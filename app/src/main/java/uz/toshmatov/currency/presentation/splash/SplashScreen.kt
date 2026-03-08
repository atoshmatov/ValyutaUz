package uz.toshmatov.currency.presentation.splash

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyPreview
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.MainActivity
import uz.toshmatov.currency.presentation.main.MainScreen

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
            val context = LocalContext.current
            val activity = context as? Activity
            val launchIntent = activity?.intent

            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp

            AsyncImage(
                modifier = Modifier
                    .size(screenWidth / 1.4f),
                model = drawable.ic_launcher_foreground,
                contentDescription = string.app_name.resource,
            )
            LaunchedEffect(Unit) {
                delay(2000)
                val shouldOpenConverter = launchIntent
                    ?.getBooleanExtra(MainActivity.EXTRA_OPEN_CONVERTER, false)
                    ?: false
                if (shouldOpenConverter && launchIntent != null) {
                    val code = launchIntent
                        .getStringExtra(MainActivity.EXTRA_CONVERTER_CODE)
                        .orEmpty()
                        .trim()
                    val rate = launchIntent
                        .getStringExtra(MainActivity.EXTRA_CONVERTER_RATE)
                        .orEmpty()
                        .trim()
                    if (code.isNotBlank() && rate.isNotBlank()) {
                        val codeName = launchIntent
                            .getStringExtra(MainActivity.EXTRA_CONVERTER_CODE_NAME)
                            .orEmpty()
                            .trim()
                            .ifBlank { code }
                        navigator?.replace(
                            MainScreen(
                                initialConverterCodeName = codeName,
                                initialConverterCode = code,
                                initialConverterRate = rate
                            )
                        )
                    } else {
                        navigator?.replace(MainScreen())
                    }
                } else {
                    navigator?.replace(MainScreen())
                }
            }
        }
    }
}

@CurrencyPreview
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
