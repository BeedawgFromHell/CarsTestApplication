package kg.rkd.carstestapplication.domain


interface CarsInteractor {
    suspend fun getCars(): List<CarModel>
    suspend fun saveCar(car: CarModel)
}