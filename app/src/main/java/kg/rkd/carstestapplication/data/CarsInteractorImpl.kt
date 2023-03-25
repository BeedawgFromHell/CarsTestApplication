package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.AppConfig
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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

class CarsInteractorImplWithBilling(
    private val interactor: CarsInteractor,
    private val billingRepository: BillingRepository
) : CarsInteractor by interactor {

    override fun getCars(): Flow<List<CarModel>> {
        return if (billingRepository.isSubscribed(BillingRepository.Products.SUBSCRIPTION)) {
            interactor.getCars()
        } else {
            interactor.getCars().map { list ->
                list.mapIndexed { index, car ->
                    if (index > AppConfig.CARS_WITHOUT_SUBSCRIPTION - 1) {
                        car.copy(
                            shouldBeBlurred = true
                        )
                    } else car
                }
            }
        }
    }

}