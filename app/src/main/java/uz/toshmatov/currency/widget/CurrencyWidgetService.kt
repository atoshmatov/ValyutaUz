package uz.toshmatov.currency.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import uz.toshmatov.currency.R
import uz.toshmatov.currency.core.extensions.CurrencyCode
import uz.toshmatov.currency.presentation.MainActivity

class CurrencyWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        val appWidgetId = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
        return CurrencyWidgetRemoteViewsFactory(
            context = applicationContext,
            appWidgetId = appWidgetId
        )
    }
}

private class CurrencyWidgetRemoteViewsFactory(
    private val context: Context,
    private val appWidgetId: Int
) : RemoteViewsService.RemoteViewsFactory {

    private var items: List<WidgetRateItem> = emptyList()
    private val positiveColor by lazy {
        ContextCompat.getColor(context, R.color.widget_diff_positive)
    }
    private val negativeColor by lazy {
        ContextCompat.getColor(context, R.color.widget_diff_negative)
    }

    override fun onCreate() {
        items = CurrencyWidgetStorage.readRates(context, appWidgetId)
    }

    override fun onDataSetChanged() {
        items = CurrencyWidgetStorage.readRates(context, appWidgetId)
    }

    override fun onDestroy() {
        items = emptyList()
    }

    override fun getCount(): Int = items.size

    override fun getViewAt(position: Int): RemoteViews {
        val item = items.getOrNull(position) ?: return RemoteViews(
            context.packageName,
            R.layout.widget_currency_row
        )
        val isNegative = item.diff.trim().startsWith("-")
        val diffText = if (isNegative) item.diff else "+${item.diff}"

        return RemoteViews(context.packageName, R.layout.widget_currency_row).apply {
            setImageViewResource(R.id.widget_row_flag, resolveFlag(item.code))
            setTextViewText(R.id.widget_row_code, item.code)
            setTextViewText(R.id.widget_row_rate, item.rate)
            setTextViewText(R.id.widget_row_diff, diffText)
            setTextColor(
                R.id.widget_row_diff,
                if (isNegative) negativeColor else positiveColor
            )
            setOnClickFillInIntent(
                R.id.widget_row_root,
                Intent().apply {
                    putExtra(MainActivity.EXTRA_OPEN_CONVERTER, true)
                    putExtra(MainActivity.EXTRA_CONVERTER_CODE_NAME, item.codeName)
                    putExtra(MainActivity.EXTRA_CONVERTER_CODE, item.code)
                    putExtra(MainActivity.EXTRA_CONVERTER_RATE, item.rate)
                }
            )
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    private fun resolveFlag(code: String): Int {
        return runCatching { CurrencyCode.valueOf(code.uppercase()).flag }
            .getOrDefault(R.drawable.ic_empty_flag)
    }
}
