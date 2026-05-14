package uz.toshmatov.currency.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import uz.toshmatov.currency.data.local.room.dao.CBUDao
import uz.toshmatov.currency.data.local.room.dao.CurrencyHistoryDao
import uz.toshmatov.currency.data.local.room.entity.CBUEntity
import uz.toshmatov.currency.data.local.room.entity.CurrencyHistoryEntity

@Database(
    entities = [CBUEntity::class, CurrencyHistoryEntity::class],
    version = 2
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun getCBUDao(): CBUDao
    abstract fun getCurrencyHistoryDao(): CurrencyHistoryDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `currency_history` " +
                    "(`currencyCode` TEXT NOT NULL, `date` TEXT NOT NULL, `rate` REAL NOT NULL, " +
                    "PRIMARY KEY(`currencyCode`, `date`))"
                )
            }
        }
    }
}
