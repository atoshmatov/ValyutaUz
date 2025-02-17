package uz.toshmatov.currency.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.toshmatov.currency.data.Configurations.CBU_BASE_URL
import uz.toshmatov.currency.data.Configurations.CONNECTION_TIMEOUT_SECONDS
import uz.toshmatov.currency.data.local.prefs.Prefs
import uz.toshmatov.currency.data.remote.api.CBUApiService
import uz.toshmatov.currency.data.remote.retrofit.adapter.CoroutineCallAdapterFactory
import uz.toshmatov.currency.data.remote.retrofit.adapter.FlowCallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofitCbu(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(CBU_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providesCBUService(
        retrofit: Retrofit,
    ): CBUApiService {
        return retrofit.create(CBUApiService::class.java)
    }

    @Provides
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun providePrefs(context: Context): Prefs {
        return Prefs(context)
    }
}