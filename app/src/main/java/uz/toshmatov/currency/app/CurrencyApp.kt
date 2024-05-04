package uz.toshmatov.currency.app

import android.app.Application
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import uz.toshmatov.currency.BuildConfig
import uz.toshmatov.currency.core.logger.Logger

@HiltAndroidApp
class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setup(BuildConfig.DEBUG)

        Lingver.init(this)
    }
}
