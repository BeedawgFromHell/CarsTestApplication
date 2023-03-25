package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.domain.SettingsInteractor
import kotlinx.coroutines.flow.Flow

class SettingsInteractorImpl(
    private val billingRepository: BillingRepository,
    private val carsRepositoryImpl: CarsRepositoryCarsSavedDecorator
): SettingsInteractor {
    override fun isSubscribed(): Flow<Boolean> {
        return billingRepository.isSubscribedAsFlow(BillingRepository.Products.SUBSCRIPTION)
    }

    override fun getCarsSavedByUserCount(): Flow<Int> {
        carsRepositoryImpl.getCarsSavedByUserCount()
    }

    override fun restoreData() {
        TODO("Not yet implemented")
    }

}