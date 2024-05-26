package uz.toshmatov.currency.core.utils

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import uz.toshmatov.currency.R

typealias string = R.string
typealias drawable = R.drawable
typealias font = R.font
typealias raw = R.raw

@get:SuppressLint("SupportAnnotationUsage")
@get:StringRes
val Int.resource: String
    @Composable
    get() = stringResource(id = this)
