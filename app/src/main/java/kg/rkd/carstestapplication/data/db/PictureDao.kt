package kg.rkd.carstestapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PictureDao {

    @Insert
    suspend fun insert(item: PictureEntity): Long

    @Query("SELECT * FROM pictures WHERE id =:id")
    suspend fun getBy(id: Long): PictureEntity?

}