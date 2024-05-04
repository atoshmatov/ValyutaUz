package uz.toshmatov.currency.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.toshmatov.currency.data.local.room.entity.CBUEntity

@Dao
interface NBUDao : BaseDao<CBUEntity> {

    @Query("SELECT * FROM cbu")
    fun getCBUData(): Flow<List<CBUEntity>>
}