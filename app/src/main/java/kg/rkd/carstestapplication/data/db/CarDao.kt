package kg.rkd.carstestapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Insert
    suspend fun insert(item: CarEntity)

    @Insert
    suspend fun insert(items: List<CarEntity>)

    @Query("""SELECT * FROM cars""")
    fun getAsFlow(): Flow<List<CarEntity>>
}