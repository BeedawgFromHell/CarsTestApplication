package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CarsInteractorImpl(
    private val carsRepository: CarsRepository
) : CarsInteractor {

    override fun getCars(): Flow<List<CarModel>> {
        return carsRepository.getCars().flowOn(Dispatchers.IO)

    }

    override suspend fun saveCar(car: CarModel) {
        withContext(Dispatchers.IO) {
            carsRepository.insert(car)
        }
    }
}