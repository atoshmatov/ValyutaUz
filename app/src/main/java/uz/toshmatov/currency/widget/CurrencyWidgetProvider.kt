package uz.toshmatov.currency.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.RemoteViews
import uz.toshmatov.currency.R
import uz.toshmatov.currency.data.local.prefs.PrefKeys
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.presentation.MainActivity
import uz.toshmatov.currency.work.CurrencyWorkScheduler
import androidx.core.net.toUri

class CurrencyWidgetProvider : AppWidgetProvider() {
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        CurrencyWorkScheduler.enqueueImmediateUpdate(context = context, sendNotification = false)
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)
        appWidgetIds.forEach { widgetId ->
            CurrencyWidgetStorage.clearRates(context, widgetId)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { widgetId ->
            val cachedRates = CurrencyWidgetStorage.readRates(context, widgetId).map { item ->
                CBUModel(
                    id = 0,
                    code = item.code,
                    ccy = item.code,
                    ccyName = item.codeName,
                    nominal = "",
                    rate = item.rate,
                    diff = item.diff,
                    date = context.getString(R.string.widget_loading)
                )
            }
            updateWidgetViews(
                context = context,
                appWidgetManager = appWidgetManager,
                appWidgetId = widgetId,
                date = context.getString(R.string.widget_loading),
                selectedRates = cachedRates,
                emptyText = context.getString(R.string.widget_loading),
                isLoading = true
            )
        }
        CurrencyWorkScheduler.enqueueImmediateUpdate(context = context, sendNotification = false)
    }

    companion object {
        private const val DATA_STALE_WINDOW_MILLIS = 6 * 60 * 60 * 1000L

        fun updateWidgets(context: Context, selectedRates: List<CBUModel>) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, CurrencyWidgetProvider::class.java)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            val date = selectedRates.firstOrNull()?.date ?: context.getString(R.string.widget_no_data)

            appWidgetIds.forEach { widgetId ->
                updateWidgetViews(
                    context = context,
                    appWidgetManager = appWidgetManager,
                    appWidgetId = widgetId,
                    date = date,
                    selectedRates = selectedRates,
                    emptyText = context.getString(R.string.widget_empty_selection),
                    isLoading = false
                )
            }
        }

        private fun updateWidgetViews(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            date: String,
            selectedRates: List<CBUModel>,
            emptyText: String,
            isLoading: Boolean
        ) {
            val widgetItems = selectedRates.map { model ->
                WidgetRateItem(
                    code = model.ccy,
                    codeName = model.ccyName,
                    rate = model.rate,
                    diff = model.diff
                )
            }
            CurrencyWidgetStorage.saveRates(context, appWidgetId, widgetItems)

            val serviceIntent = Intent(context, CurrencyWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = toUri(Intent.URI_INTENT_SCHEME).toUri()
            }

            val remoteViews = RemoteViews(context.packageName, R.layout.widget_currency_rates)
            remoteViews.setTextViewText(R.id.widget_title, context.getString(R.string.app_name))
            remoteViews.setTextViewText(
                R.id.widget_date,
                context.getString(R.string.home_update_date) + ": " + date
            )
            val isStale = isWidgetDataStale(context)
            val isOffline = !isInternetAvailable(context)
            val statusText = when {
                isStale && isOffline -> context.getString(R.string.home_stale_offline_message)
                isStale -> context.getString(R.string.home_stale_title)
                else -> ""
            }
            remoteViews.setTextViewText(R.id.widget_status, statusText)
            remoteViews.setViewVisibility(
                R.id.widget_status,
                if (statusText.isBlank()) View.GONE else View.VISIBLE
            )
            remoteViews.setTextViewText(R.id.widget_empty, emptyText)
            remoteViews.setViewVisibility(
                R.id.widget_empty,
                if (widgetItems.isEmpty()) View.VISIBLE else View.GONE
            )
            remoteViews.setViewVisibility(
                R.id.widget_loading_indicator,
                if (isLoading) View.VISIBLE else View.GONE
            )
            remoteViews.setRemoteAdapter(R.id.widget_list, serviceIntent)
            remoteViews.setEmptyView(R.id.widget_list, R.id.widget_empty)
            remoteViews.setPendingIntentTemplate(
                R.id.widget_list,
                createOpenConverterPendingIntentTemplate(context)
            )
            remoteViews.setOnClickPendingIntent(
                R.id.widget_root,
                createOpenAppPendingIntent(context)
            )

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list)
        }

        private fun createOpenAppPendingIntent(context: Context): PendingIntent {
            return PendingIntent.getActivity(
                context,
                4201,
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun isWidgetDataStale(context: Context): Boolean {
            val lastUpdate = Prefs(context).get(PrefKeys.CBU_DATE_KEY, 0L)
            if (lastUpdate <= 0L) return false
            return System.currentTimeMillis() - lastUpdate >= DATA_STALE_WINDOW_MILLIS
        }

        private fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }

        private fun createOpenConverterPendingIntentTemplate(context: Context): PendingIntent {
            val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_MUTABLE
                } else {
                    0
                }
            return PendingIntent.getActivity(
                context,
                4202,
                Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
                pendingIntentFlags
            )
        }
    }
}
