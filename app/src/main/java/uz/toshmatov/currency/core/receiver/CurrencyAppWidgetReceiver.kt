package uz.toshmatov.currency.core.receiver

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import uz.toshmatov.currency.presentation.appwidget.CurrencyAppWidget

class CurrencyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CurrencyAppWidget()
}