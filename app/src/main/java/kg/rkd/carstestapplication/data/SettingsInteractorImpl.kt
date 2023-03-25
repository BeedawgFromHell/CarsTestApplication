package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.AppConfig
import kg.rkd.carstestapplication.domain.SettingsInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsInteractorImpl(
    private val billingRepository: BillingRepository,
    private val carsRepositoryImpl: CarsRepositoryCarsSavedDecorator
): SettingsInteractor {
    override fun isSubscribed(): Flow<Boolean> {
        return billingRepository.isSubscribedAsFlow(BillingRepository.Products.SUBSCRIPTION)
    }

    override fun getTriesToSaveCar(): Flow<Int> {
        return carsRepositoryImpl.getCarsSavedByUserCountAsFlow().map {
            AppConfig.ADD_CAR_LIMIT - it
        }
    }

    override suspend fun reset() {
        billingRepository.unSub(BillingRepository.Products.SUBSCRIPTION)
        carsRepositoryImpl.resetCount()
    }
}