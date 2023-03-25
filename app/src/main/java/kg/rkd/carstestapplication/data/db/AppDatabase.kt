package kg.rkd.carstestapplication.data.db

import android.content.Context
import android.util.Base64
import androidx.annotation.RawRes
import androidx.core.util.toClosedRange
import androidx.core.util.toRange
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kg.rkd.carstestapplication.R

@Database(
    entities = [
        CarEntity::class,
        PictureEntity::class,
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
    abstract fun pictureDao(): PictureDao
}

class StartingCars(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        try {

            getCarPictures().forEach {
                db.execSQL(
                    """
                        INSERT INTO pictures (id, picture)
                        VALUES (${it.id}, '${it.picture}')
                    """.trimIndent()
                )
            }

            getCars().forEach {
                db.execSQL(
                    """
                    INSERT INTO cars (id,name,photoId,year,created, engine_capacity)
                    VALUES (${it.id}, '${it.name}', '${it.photoId}', ${it.year}, ${it.created}, ${it.engineCapacity})
                """.trimIndent()
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getCars(): List<CarEntity> {
        return listOf(
            CarEntity(
                id = 1,
                name = "Audi",
                photoId = 1,
                year = 2020,
                created = System.currentTimeMillis(),
                engineCapacity = 4.5f,
            ),
            CarEntity(
                id = 2,
                name = "BMW X6",
                photoId = 2,
                year = 2016,
                created = System.currentTimeMillis(),
                engineCapacity = 5.0f
            ),
            CarEntity(
                id = 3,
                name = "Lexus RX",
                photoId = 3,
                year = 2021,
                created = System.currentTimeMillis(),
                engineCapacity = 3.5f
            ),
            CarEntity(
                id = 4,
                name = "Optimus Prime",
                photoId = 4,
                year = 2009,
                created = System.currentTimeMillis(),
                engineCapacity = 5.5f
            ),
            CarEntity(
                id = 5,
                name = "Moskvich",
                photoId = 5,
                year = 2022,
                created = System.currentTimeMillis(),
                engineCapacity = 2.5f
            )
        )
    }

    private fun getCarPictures(): List<PictureEntity> {
        return listOf(
            PictureEntity(
                id = 1,
                getCarPhotoAsBase64(R.raw.audi)
            ),
            PictureEntity(
                id = 2,
                getCarPhotoAsBase64(R.raw.bmwx6)
            ),
            PictureEntity(
                id = 3,
                getCarPhotoAsBase64(R.raw.lexusrx)
            ),
            PictureEntity(
                id = 4,
                getCarPhotoAsBase64(R.raw.optimus)
            ),
            PictureEntity(
                id = 5,
                getCarPhotoAsBase64(R.raw.moskvich)
            )
        )
    }

    private fun getCarPhotoAsBase64(@RawRes id: Int): String {
        val stream = context.resources.openRawResource(id)
        val bytes = stream.readBytes()
        stream.close()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}