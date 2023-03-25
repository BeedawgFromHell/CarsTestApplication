package kg.rkd.carstestapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery

@Dao
interface PictureDao {

    @Insert
    suspend fun insert(item: PictureEntity): Long

    @Query("SELECT * FROM pictures WHERE id =:id")
    suspend fun getBy(id: Long): PictureEntity?

    @RawQuery(observedEntities = [PictureEntity::class])
    suspend fun getByQuery(query: SimpleSQLiteQuery): PictureEntity
}