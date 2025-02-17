package uz.toshmatov.currency.presentation.main.tabscreen.setting.feature.theme.component

import androidx.annotation.StringRes
import uz.toshmatov.currency.data.local.model.ThemeMode

data class ThemeModel(
    @StringRes val title: Int,
    val icon: Int,
    val themeMode: ThemeMode
)
