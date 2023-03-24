package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.data.db.CarDao
import kg.rkd.carstestapplication.data.db.CarEntity
import kg.rkd.carstestapplication.domain.CarModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

interface CarsRepository {
    suspend fun getCars(): List<CarModel>
    suspend fun insert(item: CarModel)
}

class CarsRepositoryImpl(
    private val dao: CarDao
): CarsRepository {
    override suspend fun getCars(): List<CarModel> {
        return dao.getAsFlow().map { CarModel(
            id = it.id,
            name = it.name,
            photo = it.photo,
            year = it.year,
            engineCapacity = it.engineCapacity,
            createdDate = it.created
        ) }.toList()
    }

    override suspend fun insert(item: CarModel) {
        dao.insert(
            CarEntity(
                name = item.name,
                photo = item.photo,
                year = item.year,
                engineCapacity = item.engineCapacity,
                created = item.createdDate
            )
        )
    }
}