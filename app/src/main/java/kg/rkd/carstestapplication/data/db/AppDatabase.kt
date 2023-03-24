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
        CarEntity::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao
}

class StartingCars(private val context: Context) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        try {
            getCars().forEach {
                db.execSQL(
                    """
                    INSERT INTO cars (name,photo,year,created)
                    VALUES ('${it.name}', '${it.photo}', ${it.year}, ${it.created}
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
                name = "Audi",
                photo = getCarPhotoAsBase64(R.raw.Audi),
                year = 2020,
                created = System.currentTimeMillis(),
                engineCapacity = 4.5f,
            ),
            CarEntity(
                name = "BMW X6",
                photo = getCarPhotoAsBase64(R.raw.BMWX6),
                year = 2016,
                created = System.currentTimeMillis(),
                engineCapacity = 5.0f
            ),
            CarEntity(
                name = "Lexus RX",
                photo = getCarPhotoAsBase64(R.raw.LexusRx),
                year = 2021,
                created = System.currentTimeMillis(),
                engineCapacity = 3.5f
            ),
            CarEntity(
                name = "Optimus Prime",
                photo = getCarPhotoAsBase64(R.raw.Optimus),
                year = 2009,
                created = System.currentTimeMillis(),
                engineCapacity = 5.5f
            ),
            CarEntity(
                name = "Moskvich",
                photo = getCarPhotoAsBase64(R.raw.Moskvich),
                year = 2022,
                created = System.currentTimeMillis(),
                engineCapacity = 2.5f
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