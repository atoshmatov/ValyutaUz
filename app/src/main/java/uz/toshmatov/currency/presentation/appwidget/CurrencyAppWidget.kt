package uz.toshmatov.currency.presentation.appwidget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import uz.toshmatov.currency.core.utils.drawable


class CurrencyAppWidget : GlanceAppWidget() {

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            AppWidgetContent()
        }
    }

    @Composable
    fun AppWidgetContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp)
                .background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Image(
                modifier = GlanceModifier.size(24.dp),
                provider = ImageProvider(drawable.ic_cbu_logo),
                contentDescription = "cbu"
            )
            Text(text = "Currency")
        }
    }
}