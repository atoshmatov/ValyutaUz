package uz.toshmatov.currency.presentation.main.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

object SettingScreen : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon =
                rememberVectorPainter(ImageVector.vectorResource(id = drawable.ic_tab_setting))
            val title = string.tab_setting.resource

            return remember {
                TabOptions(
                    index = 3u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        SettingScreenContent()
    }
}

@Composable
fun SettingScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "SettingScreen")
    }
}
