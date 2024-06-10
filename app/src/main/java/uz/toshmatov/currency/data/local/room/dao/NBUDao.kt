package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.room.entity.NBUEntity

@Dao
interface NBUDao : BaseDao<NBUEntity> {

    @Query("SELECT * FROM nbu")
    fun getCBUData(): Flow<List<NBUEntity>>

    @Query("DELETE FROM nbu")
    suspend fun deleteAll()
}