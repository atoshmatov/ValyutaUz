package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.room.entity.CBUEntity

@Dao
interface CBUDao : BaseDao<CBUEntity> {
    @Query("SELECT * FROM cbu")
    fun getCBUDataList(): Flow<List<CBUEntity>>

    @Query("DELETE FROM cbu")
    suspend fun deleteAll()
}