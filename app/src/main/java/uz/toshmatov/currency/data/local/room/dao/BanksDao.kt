package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import uz.toshmatov.currency.data.local.room.entity.BanksEntity

@Dao
interface BanksDao : BaseDao<BanksEntity> {

    @Query("SELECT * FROM banks")
    fun getAll(): List<BanksEntity>
}