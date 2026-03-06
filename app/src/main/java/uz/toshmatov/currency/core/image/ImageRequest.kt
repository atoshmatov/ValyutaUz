package uz.toshmatov.currency.core.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import uz.toshmatov.currency.core.extensions.CurrencyCode

@Composable
fun String.imageToRequest(): Any {
    val flag = runCatching { CurrencyCode.valueOf(this).flag }.getOrNull()
    return ImageRequest.Builder(LocalContext.current)
        .data(flag).crossfade(true).build()
}

