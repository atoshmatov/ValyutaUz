package uz.toshmatov.currency.di

import android.app.Application
import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.jsoup.Connection
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.toshmatov.currency.data.Configurations
import uz.toshmatov.currency.data.Configurations.CBU_BASE_URL
import uz.toshmatov.currency.data.Configurations.CONNECTION_TIMEOUT_SECONDS
import uz.toshmatov.currency.data.Configurations.NBU_BASE_URL
import uz.toshmatov.currency.data.remote.api.CBUApiService
import uz.toshmatov.currency.data.remote.api.NBUApiService
import uz.toshmatov.currency.data.remote.retrofit.adapter.CoroutineCallAdapterFactory
import uz.toshmatov.currency.data.remote.retrofit.adapter.FlowCallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

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

    @[Provides Named("CBU")]
    fun provideRetrofitCbu(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(CBU_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideContext(): Connection {
        return Jsoup.newSession()
            .timeout(20 * 1000)
            .userAgent("FooBar 2000").url(Configurations.DOLLER_UZ)
    }

    @[Provides Named("NBU")]
    fun provideRetrofitNbu(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        return Retrofit.Builder()
            .baseUrl(NBU_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addCallAdapterFactory(FlowCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesCBUService(
        @Named("CBU") retrofit: Retrofit,
    ): CBUApiService {
        return retrofit.create(CBUApiService::class.java)
    }

    @Provides
    fun providesNBUService(
        @Named("NBU") retrofit: Retrofit,
    ): NBUApiService {
        return retrofit.create(NBUApiService::class.java)
    }

    @[Singleton Provides]
    fun providesContext(application: Application): Context {
        return application.applicationContext
    }
}