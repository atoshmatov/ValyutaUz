package uz.toshmatov.currency.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.toshmatov.currency.core.connect.ConnectivityObserver
import uz.toshmatov.currency.core.connect.NetworkConnectivityObserver
import uz.toshmatov.currency.data.local.repository.DataStoreRepositoryImpl
import uz.toshmatov.currency.data.remote.repository.CBURepositoryImpl
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.DataStoreRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    fun bindCBURepository(repositoryImpl: CBURepositoryImpl): CBURepository

    @Binds
    @Singleton
    fun bindConnectivityObserver(connectivityObserver: NetworkConnectivityObserver): ConnectivityObserver

    @Binds
    @Singleton
    fun bindAppDataStoreRepository(appDataStore: DataStoreRepositoryImpl): DataStoreRepository
}
