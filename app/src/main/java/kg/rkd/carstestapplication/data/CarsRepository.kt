package kg.rkd.carstestapplication.data

import android.util.Base64
import androidx.sqlite.db.SimpleSQLiteQuery
import kg.rkd.carstestapplication.data.db.CarDao
import kg.rkd.carstestapplication.data.db.CarEntity
import kg.rkd.carstestapplication.data.db.PictureDao
import kg.rkd.carstestapplication.data.db.PictureEntity
import kg.rkd.carstestapplication.domain.CarModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface CarsRepository {
    fun getCars(): Flow<List<CarModel>>
    suspend fun insert(item: CarModel)
}

class CarsRepositoryImpl(
    private val carDao: CarDao,
    private val pictureDao: PictureDao,

    ) : CarsRepository {
    override fun getCars(): Flow<List<CarModel>> {
        return carDao.getAsFlow().map { list ->
            list.map {

                val picture = pictureDao.getByQuery(
                    SimpleSQLiteQuery("SELECT * FROM pictures WHERE id = ?", arrayOf(it.photoId))
                )
                CarModel(
                    id = it.id,
                    name = it.name,
                    photo = Base64.decode(picture.picture, Base64.DEFAULT),
                    year = it.year,
                    engineCapacity = it.engineCapacity,
                    createdDate = it.created
                )
            }
        }
    }

    override suspend fun insert(item: CarModel) {
        carDao.insert(
            CarEntity(
                name = item.name,
                photoId = pictureDao.insert(
                    PictureEntity(
                        picture = Base64.encodeToString(item.photo, Base64.DEFAULT)
                    )
                ),
                year = item.year,
                engineCapacity = item.engineCapacity,
                created = item.createdDate
            )
        )
    }
}
