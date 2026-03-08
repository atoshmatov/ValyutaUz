package uz.toshmatov.currency.core.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import uz.toshmatov.currency.R
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.presentation.MainActivity

object CurrencyNotification {
    const val CHANNEL_ID = "daily_currency_channel"
    private const val CHANNEL_NAME = "Daily Currency Updates"
    private const val CHANNEL_DESCRIPTION = "Daily selected currency rates"
    private const val NOTIFICATION_ID = 20260308

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }
        manager.createNotificationChannel(channel)
    }

    fun showDailyRates(context: Context, selectedRates: List<CBUModel>) {
        if (selectedRates.isEmpty()) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentIntent = PendingIntent.getActivity(
            context,
            1001,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val previewText = selectedRates
            .take(3)
            .joinToString(separator = " • ") { model ->
                "${model.ccy}: ${model.rate}"
            }
        val topItems = selectedRates.take(3)
        val inboxStyle = NotificationCompat.InboxStyle()
            .setBigContentTitle(context.getString(R.string.notification_title_daily))
        topItems.forEach { model ->
            inboxStyle.addLine("${model.ccy}  ${model.rate}  (${model.diff})")
        }
        if (selectedRates.size > topItems.size) {
            inboxStyle.addLine(
                context.getString(
                    R.string.widget_more_count,
                    selectedRates.size - topItems.size
                )
            )
        }
        val updateDate = selectedRates.firstOrNull()?.date.orEmpty()

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_update)
            .setContentTitle(context.getString(R.string.notification_title_daily))
            .setContentText(previewText)
            .setSubText(context.getString(R.string.home_update_date) + ": " + updateDate)
            .setStyle(inboxStyle)
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }
}
