package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.appinfo

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.BuildConfig
import uz.toshmatov.currency.core.extensions.openEmail
import uz.toshmatov.currency.core.extensions.openRateAppPage
import uz.toshmatov.currency.core.extensions.openShareAppLink
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTheme
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.CurrencyIcon
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string

class InfoScreen : Screen {
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
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(CurrencyColors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(
            titleId = string.settings_info,
            onBackClick = backClick,
            contentDescription = "Back"
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
        AppInfoHeaderCard()
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
        InfoDescriptionCard()
        Spacer(modifier = Modifier.height(CurrencyDimensions.small))
        InfoActionCard(
            icon = drawable.ic_contact,
            title = string.settings_contact.resource,
            onClick = { context.openEmail() }
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.extraSmall))
        InfoActionCard(
            icon = drawable.ic_star,
            title = string.settings_rate.resource,
            onClick = { context.openRateAppPage() }
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.extraSmall))
        InfoActionCard(
            icon = drawable.ic_share,
            title = string.settings_share.resource,
            onClick = { context.openShareAppLink() }
        )
        Spacer(modifier = Modifier.height(CurrencyDimensions.large))
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}

@Composable
private fun AppInfoHeaderCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        shape = RoundedCornerShape(CurrencyDimensions.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = CurrencyColors.bottomBar
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CurrencyDimensions.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrencyIcon(
                image = drawable.ic_launcher_foreground,
                modifier = Modifier
                    .size(56.dp),
                size = 40.dp,
                padding = 8.dp,
                shape = CircleShape,
                color = CurrencyColors.background
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium)
            ) {
                Text(
                    text = string.app_name.resource,
                    style = CurrencyTypography.buttonRegular,
                    color = CurrencyColors.text
                )
                Text(
                    text = string.settings_info.resource,
                    style = CurrencyTypography.captionUppercase,
                    color = CurrencyColors.textSecondary
                )
            }
            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                style = CurrencyTypography.captionRegular,
                color = CurrencyColors.button,
                modifier = Modifier
                    .background(
                        color = CurrencyColors.bottomBarIndicator,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun InfoDescriptionCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        shape = RoundedCornerShape(CurrencyDimensions.small),
        colors = CardDefaults.cardColors(
            containerColor = CurrencyColors.bottomBar
        )
    ) {
        Text(
            text = string.settings_info_title.resource,
            modifier = Modifier.padding(CurrencyDimensions.medium),
            color = CurrencyColors.textSecondary,
            style = CurrencyTypography.captionUppercase
        )
    }
}

@Composable
private fun InfoActionCard(
    modifier: Modifier = Modifier,
    icon: Int,
    title: String,
    onClick: () -> Unit
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        onClick = onClick,
        shape = RoundedCornerShape(CurrencyDimensions.medium),
        border = BorderStroke(
            width = if (isDarkTheme) 1.dp else 0.8.dp,
            color = if (isDarkTheme) {
                CurrencyColors.textSecondary.copy(alpha = 0.15f)
            } else {
                CurrencyColors.textSecondary.copy(alpha = 0.12f)
            }
        ),
        colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = CurrencyDimensions.medium,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        color = CurrencyColors.button.copy(alpha = if (isDarkTheme) 0.24f else 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = CurrencyColors.button
                )
            }
            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium),
                style = CurrencyTypography.labelSemiMedium,
                color = CurrencyColors.text
            )
            CurrencyIcon(
                image = drawable.ic_right,
                tint = CurrencyColors.textSecondary,
                size = 16.dp,
                contentDescription = "Go"
            )
        }
    }
}

@Preview
@Composable
private fun AppInfoPreview() = CurrencyTheme {
    InfoScreenContent {}
}
