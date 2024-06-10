package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T)

    @Upsert
    fun upsert(t: List<T>)

    @Update
    fun upDate(t: T)

    @Delete
    fun delete(t: T)
}