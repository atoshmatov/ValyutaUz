package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.data.local.model.AccentColor
import uz.toshmatov.currency.data.local.model.ThemeMode
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents.ThemeEvents
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.intents.ThemeState

class ThemeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<ThemeViewModel>()
        val state by viewModel.state.collectAsState()

        ThemeContent(
            state = state,
            reduce = viewModel::reduce,
            backClick = navigator::pop
        )
    }
}

@Composable
fun ThemeContent(
    state: ThemeState,
    reduce: (ThemeEvents) -> Unit,
    modifier: Modifier = Modifier,
    backClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CurrencyColors.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(
            titleId = string.settings_theme,
            onBackClick = backClick,
            contentDescription = "Back"
        )

        Column(
            modifier = Modifier
                .padding(horizontal = CurrencyDimensions.medium)
                .padding(top = CurrencyDimensions.medium),
            verticalArrangement = Arrangement.spacedBy(CurrencyDimensions.medium)
        ) {
            ThemeModeSection(
                currentMode = state.currentThemeMode,
                onModeSelect = { reduce(ThemeEvents.UpdateTheme(it)) }
            )
            AccentColorSection(
                currentAccent = state.currentAccentColor,
                onColorSelect = { reduce(ThemeEvents.UpdateAccentColor(it)) }
            )
        }
    }
}

@Composable
private fun ThemeModeSection(
    currentMode: ThemeMode,
    onModeSelect: (ThemeMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResource(string.settings_theme),
            style = CurrencyTypography.textSemiBold,
            color = CurrencyColors.text
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ThemeModeChip(
                modifier = Modifier.weight(1f),
                label = stringResource(string.settings_system),
                icon = drawable.ic_system,
                selected = currentMode == ThemeMode.System,
                onClick = { onModeSelect(ThemeMode.System) }
            )
            ThemeModeChip(
                modifier = Modifier.weight(1f),
                label = stringResource(string.settings_light),
                icon = drawable.ic_light,
                selected = currentMode == ThemeMode.Light,
                onClick = { onModeSelect(ThemeMode.Light) }
            )
            ThemeModeChip(
                modifier = Modifier.weight(1f),
                label = stringResource(string.settings_dark),
                icon = drawable.ic_dark,
                selected = currentMode == ThemeMode.Dark,
                onClick = { onModeSelect(ThemeMode.Dark) }
            )
        }
    }
}

@Composable
private fun ThemeModeChip(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    val accent = CurrencyColors.button
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(if (selected) accent.copy(alpha = 0.12f) else CurrencyColors.itemBackground)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) accent else CurrencyColors.itemBackground,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = label,
            tint = if (selected) accent else CurrencyColors.textSecondary,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            style = CurrencyTypography.captionRegular,
            color = if (selected) accent else CurrencyColors.textSecondary
        )
    }
}

@Composable
private fun AccentColorSection(
    currentAccent: AccentColor,
    onColorSelect: (AccentColor) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Asosiy rang",
            style = CurrencyTypography.textSemiBold,
            color = CurrencyColors.text
        )
        val colors = AccentColor.entries
        val rows = colors.chunked(4)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            rows.forEach { rowColors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowColors.forEach { accent ->
                        ColorCircle(
                            modifier = Modifier.weight(1f),
                            color = accent.color,
                            selected = currentAccent == accent,
                            onClick = { onColorSelect(accent) }
                        )
                    }
                    repeat(4 - rowColors.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorCircle(
    modifier: Modifier = Modifier,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (selected) Modifier.border(3.dp, CurrencyColors.background, CircleShape)
                    else Modifier
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    painter = painterResource(drawable.ic_check),
                    contentDescription = null,
                    tint = if (color.luminance() > 0.4f) Color.Black else Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (selected) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .border(2.dp, color.copy(alpha = 0.4f), CircleShape)
            )
        }
    }
}
