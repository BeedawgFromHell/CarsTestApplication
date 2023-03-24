package kg.rkd.carstestapplication.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val photo: String,  //in Base64
    val year: Int,
    @ColumnInfo(name = "engine_capacity")
    val engineCapacity: Float,
    val created: Long, //in millis
)
