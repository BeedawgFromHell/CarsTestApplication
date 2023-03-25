package kg.rkd.carstestapplication.data

import android.util.Base64
import kg.rkd.carstestapplication.data.db.CarDao
import kg.rkd.carstestapplication.data.db.CarEntity
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.utils.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

interface CarsRepository {
    fun getCars(): Flow<List<CarModel>>
    suspend fun insert(item: CarModel)
}

class CarsRepositoryImpl(
    private val dao: CarDao
) : CarsRepository {
    override fun getCars(): Flow<List<CarModel>> {
        return dao.getAsFlow().map { list ->
            list.map {
                CarModel(
                    id = it.id,
                    name = it.name,
                    photo = Base64.decode(it.photo, Base64.DEFAULT),
                    year = it.year,
                    engineCapacity = it.engineCapacity,
                    createdDate = it.created
                )
            }
        }
    }

    override suspend fun insert(item: CarModel) {
        dao.insert(
            CarEntity(
                name = item.name,
                photo = Base64.encodeToString(item.photo, Base64.DEFAULT),
                year = item.year,
                engineCapacity = item.engineCapacity,
                created = item.createdDate
            )
        )
    }
}

interface CarsRepositoryDecorator : CarsRepository {
    fun getCarsSavedByUserCount(): Int
}

class CarsRepositoryDecoratorImpl(
    private val carsRepository: CarsRepository,
    private val prefs: AppPreferences
) : CarsRepositoryDecorator, CarsRepository by carsRepository {
    override fun getCarsSavedByUserCount(): Int = prefs.getInt(AppPreferences.SAVED_CARS_COUNT_KEY)
}