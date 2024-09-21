package uz.toshmatov.currency.presentation.main.screen.setting.feature.appinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.BuildConfig
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyIcon
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

class InfoScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        InfoScreenContent(backClick = navigator::pop)
    }
}

@Composable
fun InfoScreenContent(
    modifier: Modifier = Modifier,
    backClick: () -> Unit
) {
    val rememberScrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState)
            .background(CurrencyColors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopBar(
            titleId = string.settings_info,
            onBackClick = backClick,
            contentDescription = "Back"
        )
        CurrencyIcon(
            image = drawable.ic_launcher_foreground,
            size = 120.dp,
            padding = 0.dp,
            contentDescription = "App Icon"
        )
        Text(
            text = string.app_name.resource,
            style = CurrencyTypography.buttonRegular,
            color = CurrencyColors.text,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = string.settings_info_title.resource,
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = CurrencyDimensions.medium),
            color = CurrencyColors.textSecondary,
            style = CurrencyTypography.captionUppercase,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        /*Row(
            modifier = Modifier.padding(horizontal = CurrencyDimensions.medium),
            horizontalArrangement = Arrangement.spacedBy(CurrencyDimensions.medium)
        ) {
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        context.telegramIntent()
                    },
                painter = painterResource(id = drawable.ic_telegram),
                contentDescription = "Telegram"
            )
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { },
                painter = painterResource(id = drawable.ic_instagram),
                contentDescription = "Telegram"
            )
            Image(
                modifier = Modifier
                    .size(32.dp)
                    .clickable { },
                painter = painterResource(id = drawable.ic_linkedin),
                contentDescription = "Telegram"
            )
        }*/
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = string.version.resource + " " + BuildConfig.VERSION_NAME,
            style = CurrencyTypography.captionRegular,
            color = CurrencyColors.textSecondary,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview
@Composable
private fun AppInfoPreview() = CurrencyTheme {
    InfoScreenContent {}
}