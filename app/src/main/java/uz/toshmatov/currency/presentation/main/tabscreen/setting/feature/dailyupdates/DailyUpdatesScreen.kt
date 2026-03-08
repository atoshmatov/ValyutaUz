package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import uz.toshmatov.currency.core.image.imageToRequest
import uz.toshmatov.currency.core.theme.CurrencyColors
import uz.toshmatov.currency.core.theme.CurrencyDimensions
import uz.toshmatov.currency.core.theme.CurrencyTypography
import uz.toshmatov.currency.core.uicompoenent.TopBar
import uz.toshmatov.currency.core.utils.drawable
import uz.toshmatov.currency.core.utils.resource
import uz.toshmatov.currency.core.utils.string
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.dailyupdates.intents.DailyUpdatesState

@OptIn(ExperimentalMaterial3Api::class)
class DailyUpdatesScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getViewModel<DailyUpdatesViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        DailyUpdatesContent(
            state = state,
            onBackClick = navigator::pop,
            onNotificationEnabledChanged = viewModel::updateNotificationEnabled,
            onNotificationTimeChanged = viewModel::updateNotificationTime,
            onToggleCurrency = viewModel::toggleCurrency
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DailyUpdatesContent(
    state: DailyUpdatesState,
    onBackClick: () -> Unit,
    onNotificationEnabledChanged: (Boolean) -> Unit,
    onNotificationTimeChanged: (String) -> Unit,
    onToggleCurrency: (String) -> Unit
) {
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    val selectedCount = state.selectedCodes.size
    val sortedSelectedCodes = state.selectedCodes.toList().sorted()
    val selectedPreview = when {
        sortedSelectedCodes.isEmpty() -> string.widget_empty_selection.resource
        sortedSelectedCodes.size <= 4 -> sortedSelectedCodes.joinToString(", ")
        else -> "${sortedSelectedCodes.take(4).joinToString(", ")} +${sortedSelectedCodes.size - 4}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyColors.background)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopBar(
            titleId = string.settings_daily_updates,
            onBackClick = onBackClick,
            contentDescription = "Back"
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item(key = "summary") {
                DailySummaryCard(
                    selectedCount = selectedCount,
                    selectedPreview = selectedPreview
                )
            }
            item(key = "notification_toggle") {
                Spacer(modifier = Modifier.height(CurrencyDimensions.small))
                DailySettingCard(
                    icon = Icons.Rounded.NotificationsActive,
                    title = string.settings_daily_notification.resource,
                    subtitle = string.settings_daily_notification_desc.resource,
                    onClick = {
                        onNotificationEnabledChanged(!state.notificationEnabled)
                    },
                    trailing = {
                        Switch(
                            checked = state.notificationEnabled,
                            onCheckedChange = onNotificationEnabledChanged,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CurrencyColors.bottomBar,
                                checkedTrackColor = CurrencyColors.button,
                                checkedBorderColor = CurrencyColors.button,
                                uncheckedThumbColor = CurrencyColors.bottomBar,
                                uncheckedTrackColor = CurrencyColors.textSecondary.copy(
                                    alpha = if (isDarkTheme) 0.30f else 0.22f
                                ),
                                uncheckedBorderColor = CurrencyColors.textSecondary.copy(
                                    alpha = if (isDarkTheme) 0.36f else 0.26f
                                )
                            )
                        )
                    }
                )
            }
            item(key = "notification_time") {
                Spacer(modifier = Modifier.height(CurrencyDimensions.extraSmall))
                DailySettingCard(
                    icon = Icons.Rounded.AccessTime,
                    title = string.settings_notify_time.resource,
                    subtitle = state.notificationTime,
                    onClick = {
                        showTimePicker = true
                    },
                    trailing = {
                        Card(
                            shape = RoundedCornerShape(50),
                            colors = CardDefaults.cardColors(
                                containerColor = CurrencyColors.button.copy(alpha = 0.14f)
                            )
                        ) {
                            Text(
                                text = state.notificationTime,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                color = CurrencyColors.button,
                                style = CurrencyTypography.captionUppercase
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.height(CurrencyDimensions.medium))
                Text(
                    text = string.settings_select_currencies.resource,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CurrencyDimensions.medium),
                    style = CurrencyTypography.captionUppercase,
                    color = CurrencyColors.textSecondary
                )
                Spacer(modifier = Modifier.height(CurrencyDimensions.extraSmall))
            }
            items(
                items = state.currencyList,
                key = { it.ccy }
            ) { model ->
                CurrencySelectableItem(
                    model = model,
                    isSelected = state.selectedCodes.contains(model.ccy),
                    onClick = { onToggleCurrency(model.ccy) }
                )
            }
            item(key = "bottom_space") {
                Spacer(modifier = Modifier.height(CurrencyDimensions.medium))
            }
        }
    }

    if (showTimePicker) {
        val (initialHour, initialMinute) = state.notificationTime.parseHourMinute()
        val pickerState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onNotificationTimeChanged(
                            String.format("%02d:%02d", pickerState.hour, pickerState.minute)
                        )
                        showTimePicker = false
                    }
                ) {
                    Text(text = string.common_save.resource)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }
                ) {
                    Text(text = string.common_cancel.resource)
                }
            },
            title = {
                Text(
                    text = string.settings_notify_time.resource,
                    color = CurrencyColors.text,
                    style = CurrencyTypography.labelSemiBold
                )
            },
            text = {
                TimePicker(state = pickerState)
            },
            containerColor = CurrencyColors.bottomBar
        )
    }
}

@Composable
private fun DailySummaryCard(
    selectedCount: Int,
    selectedPreview: String,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
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
                .padding(horizontal = CurrencyDimensions.medium, vertical = 14.dp),
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
                    imageVector = Icons.Rounded.Update,
                    contentDescription = "Daily updates",
                    tint = CurrencyColors.button
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium)
            ) {
                Text(
                    text = string.settings_selected_count.resource.format(selectedCount),
                    color = CurrencyColors.text,
                    style = CurrencyTypography.labelSemiMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedPreview,
                    color = CurrencyColors.textSecondary,
                    style = CurrencyTypography.captionRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DailySettingCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CurrencyDimensions.medium),
        onClick = onClick ?: {},
        enabled = onClick != null,
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
                .padding(horizontal = CurrencyDimensions.medium, vertical = 14.dp),
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
                    imageVector = icon,
                    contentDescription = title,
                    tint = CurrencyColors.button
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = CurrencyDimensions.medium)
            ) {
                Text(
                    text = title,
                    style = CurrencyTypography.labelSemiMedium,
                    color = CurrencyColors.text
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = CurrencyTypography.captionRegular,
                    color = CurrencyColors.textSecondary
                )
            }
            trailing?.invoke()
        }
    }
}

@Composable
private fun CurrencySelectableItem(
    model: CBUModel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isDarkTheme = CurrencyColors.background.luminance() < 0.5f
    val isDiffNegative = model.diff.trim().startsWith("-")
    val diffText = if (isDiffNegative) model.diff else "+${model.diff}"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = CurrencyDimensions.medium,
                vertical = CurrencyDimensions.extraSmall
            ),
        onClick = onClick,
        shape = RoundedCornerShape(CurrencyDimensions.medium),
        border = BorderStroke(
            width = if (isSelected || isDarkTheme) 1.dp else 0.8.dp,
            color = when {
                isSelected -> CurrencyColors.button
                isDarkTheme -> CurrencyColors.textSecondary.copy(alpha = 0.15f)
                else -> CurrencyColors.textSecondary.copy(alpha = 0.12f)
            }
        ),
        colors = CardDefaults.cardColors(containerColor = CurrencyColors.bottomBar)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CurrencyDimensions.medium, vertical = 12.dp),
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
                AsyncImage(
                    model = model.ccy.imageToRequest(),
                    contentDescription = model.ccy,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = drawable.ic_empty_flag)
                )
            }
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.ccy,
                    style = CurrencyTypography.textSemiBold,
                    color = CurrencyColors.text
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = model.ccyName,
                    style = CurrencyTypography.captionRegular,
                    color = CurrencyColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = model.rate,
                style = CurrencyTypography.captionUppercase,
                color = CurrencyColors.textSecondary,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = diffText,
                style = CurrencyTypography.captionUppercase,
                color = if (isDiffNegative) CurrencyColors.error else CurrencyColors.success
            )
            if (isSelected) {
                Spacer(modifier = Modifier.size(8.dp))
                Card(
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = CurrencyColors.button)
                ) {
                    Icon(
                        painter = painterResource(id = drawable.ic_check),
                        contentDescription = model.ccy,
                        tint = CurrencyColors.bottomBar,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(12.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }
    }
}

private fun String.parseHourMinute(): Pair<Int, Int> {
    val split = split(":")
    val hour = split.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 9
    val minute = split.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
    return hour to minute
}
