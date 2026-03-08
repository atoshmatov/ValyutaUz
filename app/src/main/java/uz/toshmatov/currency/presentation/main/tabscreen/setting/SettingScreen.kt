package uz.toshmatov.currency.presentation.main.tabscreen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.presentation.main.tabscreen.setting.component.SettingItem
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.appinfo.InfoScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates.DailyUpdatesScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.language.LanguageScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.ThemeScreen
import uz.toshmatov.currency.presentation.main.tabscreen.setting.intents.SettingsState
import uz.toshmatov.currency.presentation.main.tabscreen.setting.model.ActionType

object SettingScreen : Tab {
    private fun readResolve(): Any = SettingScreen

    override val options: TabOptions
        @Composable get() {
            val icon = painterResource(id = drawable.ic_tab_setting)
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

        SettingScreenContent(
            state = state,
            onClickItem = {
                when (it) {
                    ActionType.LANGUAGE -> currentNavigator.push(LanguageScreen())
                    ActionType.THEME -> currentNavigator.push(ThemeScreen())
                    ActionType.DAILY_UPDATES -> currentNavigator.push(DailyUpdatesScreen())
                    ActionType.ABOUT_APP -> currentNavigator.push(InfoScreen())
                    ActionType.CONTACT_US,
                    ActionType.RATE_APP,
                    ActionType.SHARE_APP -> Unit
                }
            }
        )
    }
}

@Composable
private fun SettingScreenContent(
    state: SettingsState,
    onClickItem: (ActionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(
                horizontal = CurrencyDimensions.medium,
                vertical = CurrencyDimensions.small
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SettingsHeaderCard()
        Spacer(modifier = Modifier.height(CurrencyDimensions.medium))
        for (setting in state.settings) {
            SettingItem(
                modifier = Modifier.padding(bottom = CurrencyDimensions.small),
                settingModel = setting,
                onClick = onClickItem
            )
        }
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
    }
}

@Composable
private fun SettingsHeaderCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CurrencyDimensions.cornerRadius),
        colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CurrencyDimensions.medium)
        ) {
            Text(
                text = string.tab_setting.resource,
                style = CurrencyTypography.buttonRegular,
                color = CurrencyColors.text
            )
            Spacer(modifier = Modifier.height(CurrencyDimensions.extraSmall))
            Text(
                text = "${string.settings_language.resource}, ${string.settings_theme.resource}, ${string.settings_daily_updates.resource}",
                style = CurrencyTypography.textMedium,
                color = CurrencyColors.textSecondary
            )
        }
    }
}
