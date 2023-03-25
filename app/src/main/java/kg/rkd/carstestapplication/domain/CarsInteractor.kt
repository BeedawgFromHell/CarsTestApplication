package kg.rkd.carstestapplication.domain

import kotlinx.coroutines.flow.Flow


interface CarsInteractor {
    fun getCars(): Flow<List<CarModel>>
    suspend fun saveCar(car: CarModel)
}

interface CarsInteractorBillingDecorator: CarsInteractor {
    fun isAllowedToSaveCar(): Boolean
}