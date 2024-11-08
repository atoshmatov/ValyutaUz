package uz.toshmatov.currency.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.toshmatov.currency.core.connect.ConnectivityObserver
import uz.toshmatov.currency.core.connect.NetworkConnectivityObserver
import uz.toshmatov.currency.data.local.repository.DataStoreRepository
import uz.toshmatov.currency.data.local.repository.DataStoreRepositoryImpl
import uz.toshmatov.currency.data.remote.api.ExchangeBankDataSource
import uz.toshmatov.currency.data.remote.api.impl.ExchangeBankDataSouImpl
import uz.toshmatov.currency.data.remote.repository.CBURepositoryImpl
import uz.toshmatov.currency.data.remote.repository.ExchangeBankRepositoryImpl
import uz.toshmatov.currency.data.remote.repository.NBURepositoryImpl
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.ExchangeBankRepository
import uz.toshmatov.currency.domain.repository.NBURepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCBURepository(repositoryImpl: CBURepositoryImpl): CBURepository

    @Binds
    fun bindNBURepository(repositoryImpl: NBURepositoryImpl): NBURepository

    @Binds
    fun bindExchangeRateRepository(repositoryImpl: ExchangeBankRepositoryImpl): ExchangeBankRepository

    @Binds
    fun bindExchangeBankDataSource(repositoryImpl: ExchangeBankDataSouImpl): ExchangeBankDataSource

    @Binds
    fun bindConnectivityObserver(connectivityObserver: NetworkConnectivityObserver): ConnectivityObserver

    @Binds
    fun bindAppDataStoreRepository(appDataStore: DataStoreRepositoryImpl): DataStoreRepository
}