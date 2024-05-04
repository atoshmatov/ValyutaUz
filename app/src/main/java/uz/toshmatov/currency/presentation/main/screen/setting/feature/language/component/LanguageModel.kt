package uz.toshmatov.currency.presentation.main.screen.setting.feature.language.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class LanguageModel(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val langType: LangType,
    val code: String,
)
