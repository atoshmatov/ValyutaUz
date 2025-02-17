package uz.toshmatov.currency.core.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import uz.toshmatov.currency.core.extensions.CurrencyCode

@Composable
fun String.imageToRequest(): Any = ImageRequest.Builder(LocalContext.current)
    .data(CurrencyCode.valueOf(this).flag).crossfade(true).build()

