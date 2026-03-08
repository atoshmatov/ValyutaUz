package uz.toshmatov.currency.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import uz.toshmatov.currency.BuildConfig
import uz.toshmatov.currency.core.logger.Logger
import uz.toshmatov.currency.core.notification.CurrencyNotification
import javax.inject.Inject

@HiltAndroidApp
class CurrencyApp : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Logger.setup(BuildConfig.DEBUG)

        Lingver.init(this)
        CurrencyNotification.ensureChannel(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
