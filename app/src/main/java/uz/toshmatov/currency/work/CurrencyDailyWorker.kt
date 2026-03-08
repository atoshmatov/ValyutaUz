package uz.toshmatov.currency.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import uz.toshmatov.currency.core.notification.CurrencyNotification
import uz.toshmatov.currency.core.utils.Resource
import uz.toshmatov.currency.domain.model.CBUModel
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import uz.toshmatov.currency.widget.CurrencyWidgetProvider

@HiltWorker
class CurrencyDailyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cbuRepository: CBURepository,
    private val dataStoreRepository: DataStoreRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val selectedCodes = dataStoreRepository.getSelectedWidgetCodes().first()
        val dailyTime = dataStoreRepository.getDailyNotificationTime().first()
        CurrencyWorkScheduler.scheduleDaily(
            context = applicationContext,
            time = dailyTime,
            hasSelectedCurrencies = selectedCodes.isNotEmpty()
        )

        if (selectedCodes.isEmpty()) {
            CurrencyWidgetProvider.updateWidgets(
                context = applicationContext,
                selectedRates = emptyList()
            )
            return Result.success()
        }

        val latestRates = loadLatestRates()
        if (latestRates.isEmpty()) return Result.retry()

        val byCode = latestRates.associateBy { it.ccy }
        val selectedRates = selectedCodes.mapNotNull { code ->
            byCode[code]
        }

        CurrencyWidgetProvider.updateWidgets(
            context = applicationContext,
            selectedRates = selectedRates
        )

        val shouldSendNotification = inputData.getBoolean(KEY_SEND_NOTIFICATION, true) &&
            dataStoreRepository.getDailyNotificationEnabled().first()

        if (shouldSendNotification) {
            CurrencyNotification.showDailyRates(
                context = applicationContext,
                selectedRates = selectedRates
            )
        }

        return Result.success()
    }

    private suspend fun loadLatestRates(): List<CBUModel> {
        val data = cbuRepository.getCurrencyList()
            .first { resource -> resource !is Resource.Loading }

        val successData = (data as? Resource.Success)?.data.orEmpty()
        if (successData.isNotEmpty()) return successData

        return cbuRepository.getCBUCurrencyList().firstOrNull().orEmpty()
    }

    companion object {
        const val KEY_SEND_NOTIFICATION = "key_send_notification"
    }
}
