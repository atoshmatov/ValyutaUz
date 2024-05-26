package uz.toshmatov.currency.core.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.request.ImageRequest
import uz.toshmatov.currency.core.extensions.CurrencyCode

@Composable
fun ImageToRequest(code: String) {
    ImageRequest.Builder(LocalContext.current)
        .data(CurrencyCode.valueOf(code).flag).crossfade(true).build()
}
