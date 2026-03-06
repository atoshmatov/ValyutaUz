package uz.toshmatov.currency.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.toshmatov.currency.data.local.room.CurrencyDatabase
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @[Provides Singleton]
    fun getAppDatabase(@ApplicationContext context: Context): CurrencyDatabase =
        Room.databaseBuilder(context, CurrencyDatabase::class.java, "Currency")
            .fallbackToDestructiveMigration()
            .build()

    @[Provides Singleton]
    fun getProvideCBUDao(database: CurrencyDatabase): CBUDao = database.getCBUDao()
}