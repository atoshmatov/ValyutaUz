package uz.toshmatov.currency.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.toshmatov.currency.data.remote.api.ExchangeRateDataSource
import uz.toshmatov.currency.data.remote.api.impl.ExchangeRateDataSouImpl
import uz.toshmatov.currency.data.remote.repository.CBURepositoryImpl
import uz.toshmatov.currency.data.remote.repository.ExchangeRateRepositoryImpl
import uz.toshmatov.currency.data.remote.repository.NBURepositoryImpl
import uz.toshmatov.currency.domain.repository.CBURepository
import uz.toshmatov.currency.domain.repository.ExchangeRateRepository
import uz.toshmatov.currency.domain.repository.NBURepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCBURepository(repositoryImpl: CBURepositoryImpl): CBURepository

    @Binds
    fun bindNBURepository(repositoryImpl: NBURepositoryImpl): NBURepository

    @Binds
    fun bindExchangeRateRepository(repositoryImpl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    fun bindExchangeRateDataSource(repositoryImpl: ExchangeRateDataSouImpl): ExchangeRateDataSource
}