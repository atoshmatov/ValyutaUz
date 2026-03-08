package uz.toshmatov.currency.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.Calendar
import java.util.concurrent.TimeUnit

object CurrencyWorkScheduler {
    const val DAILY_WORK_NAME = "daily_currency_update_work"
    private const val IMMEDIATE_WORK_NAME = "immediate_currency_update_work"

    fun scheduleDaily(context: Context, time: String, hasSelectedCurrencies: Boolean) {
        val workManager = WorkManager.getInstance(context)
        if (!hasSelectedCurrencies) {
            workManager.cancelUniqueWork(DAILY_WORK_NAME)
            return
        }

        val (hour, minute) = parseTime(time)
        val initialDelay = calculateInitialDelay(hour, minute)

        val request = PeriodicWorkRequestBuilder<CurrencyDailyWorker>(1, TimeUnit.DAYS)
            .setInputData(
                workDataOf(
                    CurrencyDailyWorker.KEY_SEND_NOTIFICATION to true
                )
            )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            DAILY_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }

    fun enqueueImmediateUpdate(context: Context, sendNotification: Boolean = false) {
        val request = OneTimeWorkRequestBuilder<CurrencyDailyWorker>()
            .setInputData(
                workDataOf(
                    CurrencyDailyWorker.KEY_SEND_NOTIFICATION to sendNotification
                )
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            IMMEDIATE_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun parseTime(time: String): Pair<Int, Int> {
        val split = time.split(":")
        val hour = split.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 9
        val minute = split.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
        return hour to minute
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return next.timeInMillis - now.timeInMillis
    }
}
