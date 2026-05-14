package uz.toshmatov.currency.core.ads

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import android.util.Log
import uz.toshmatov.currency.BuildConfig
import uz.toshmatov.currency.core.theme.CurrencyColors

private val BANNER_AD_UNIT_ID = if (BuildConfig.DEBUG)
    "ca-app-pub-3940256099942544/9214589741"   // test
else
    "ca-app-pub-8019829901651660/6714369155"   // real

@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    val bgColor = CurrencyColors.bottomBar
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind { drawRect(bgColor) }
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(
                        AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                            context, screenWidthDp
                        )
                    )
                    adUnitId = BANNER_AD_UNIT_ID
                    setBackgroundColor(bgColor.toArgb())
                    adListener = object : AdListener() {
                        override fun onAdLoaded() {
                            Log.d("AdBanner", "loaded OK unit=$BANNER_AD_UNIT_ID")
                        }
                        override fun onAdFailedToLoad(error: LoadAdError) {
                            Log.e("AdBanner", "FAILED code=${error.code} msg=${error.message}")
                        }
                        override fun onAdImpression() {
                            Log.d("AdBanner", "impression")
                        }
                    }
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = { adView ->
                adView.setBackgroundColor(bgColor.toArgb())
            }
        )
    }
}