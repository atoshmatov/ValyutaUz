package uz.toshmatov.currency.presentation.main.screen.setting

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.screen.setting.component.SettingItem
import uz.toshmatov.currency.presentation.main.screen.setting.feature.language.LanguageScreen
import uz.toshmatov.currency.presentation.main.screen.setting.feature.theme.ThemeScreen
import uz.toshmatov.currency.presentation.main.screen.setting.intents.SettingsState
import uz.toshmatov.currency.presentation.main.screen.setting.model.ActionType


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
        val viewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.state.collectAsState()
        val currentNavigator = LocalNavigator.currentOrThrow.parent!!
        val context = LocalContext.current

        SettingScreenContent(
            state = state,
            onClickItem = {
                when (it) {
                    ActionType.LANGUAGE -> currentNavigator.push(LanguageScreen())
                    ActionType.THEME -> currentNavigator.push(ThemeScreen())
                    ActionType.CONTACT_US -> {}
                    ActionType.RATE_APP -> {}
                    ActionType.SHARE_APP -> {
                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.setType("text/plain")
                        val recipients = arrayOf("mobdevali1220@gmail.com")
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipients)
                        context.startActivity(emailIntent)
                    }
                    ActionType.ABOUT_APP -> {}
                }

            }
        )
    }
}

@Composable
fun SettingScreenContent(
    state: SettingsState,
    onClickItem: (ActionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (setting in state.settings) {
            SettingItem(
                settingModel = setting,
                onClick = onClickItem
            )
        }
    }
}
