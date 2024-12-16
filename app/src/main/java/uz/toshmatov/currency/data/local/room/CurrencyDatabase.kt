package uz.toshmatov.currency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.local.room.entity.CBUEntity

@Database(entities = [CBUEntity::class], version = 1)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun getCBUDao(): CBUDao
}