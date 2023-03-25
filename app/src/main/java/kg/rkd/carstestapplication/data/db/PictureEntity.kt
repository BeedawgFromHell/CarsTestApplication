package kg.rkd.carstestapplication.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PictureEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val picture: String, //in Base64
)
