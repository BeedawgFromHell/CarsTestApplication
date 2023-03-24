package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CarsInteractorImpl(
    private val carsRepository: CarsRepository
) : CarsInteractor {

    override suspend fun getCars(): List<CarModel> {
        return withContext(Dispatchers.IO) {
            return@withContext carsRepository.getCars()
        }
    }

    override suspend fun saveCar(car: CarModel) {
        withContext(Dispatchers.IO) {
            carsRepository.insert(car)
        }
    }
}