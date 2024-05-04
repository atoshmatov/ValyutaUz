package uz.toshmatov.currency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.toshmatov.currency.data.local.room.dao.BanksDao
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.local.room.dao.NBUDao
import uz.toshmatov.currency.data.local.room.entity.BanksEntity
import uz.toshmatov.currency.data.local.room.entity.CBUEntity
import uz.toshmatov.currency.data.local.room.entity.NBUEntity

@Database(entities = [CBUEntity::class, NBUEntity::class, BanksEntity::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun getCBUDao(): CBUDao
    abstract fun getNBUDao(): NBUDao
    abstract fun getBanksDao(): BanksDao
}