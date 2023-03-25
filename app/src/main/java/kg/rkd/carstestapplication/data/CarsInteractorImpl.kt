package kg.rkd.carstestapplication.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.scale
import kg.rkd.carstestapplication.AppConfig
import kg.rkd.carstestapplication.domain.CarModel
import kg.rkd.carstestapplication.domain.CarsInteractor
import kg.rkd.carstestapplication.domain.CarsInteractorBillingDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class CarsInteractorImpl(
    private val carsRepository: CarsRepository
) : CarsInteractor {

    override fun getCars(): Flow<List<CarModel>> {
        return carsRepository.getCars().flowOn(Dispatchers.IO)
    }

    override suspend fun saveCar(car: CarModel) {
        withContext(Dispatchers.IO) {
            carsRepository.insert(
                car.copy(
                    photo = compressImage(car.photo)
                )
            )
        }
    }

    private fun compressImage(
        imageArray: ByteArray,
        width: Int = 800,
        height: Int = 800,
        quality: Int = 80
    ): ByteArray {
        val originalBitmap = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.size)
        val scaled = originalBitmap.scale(width = width, height = height)
        val output = ByteArrayOutputStream()
        scaled.compress(Bitmap.CompressFormat.JPEG, quality, output)
        val result = output.toByteArray()
        output.close()
        return result
    }
}

class CarsInteractorImplWithBilling(
    private val interactor: CarsInteractor,
    private val billingRepository: BillingRepository,
    private val carsRepositoryDecorator: CarsRepositoryCarsSavedDecorator
) : CarsInteractorBillingDecorator, CarsInteractor by interactor {

    override fun getCars(): Flow<List<CarModel>> {
        val subFlow = billingRepository.isSubscribedAsFlow(BillingRepository.Products.SUBSCRIPTION)
        val carsFlow = interactor.getCars()

        return subFlow.combine(carsFlow) { isSubscribed, carList ->
            if (isSubscribed) {
                carList
            } else {
                carList.mapIndexed { index, car ->
                    if (index > AppConfig.CARS_WITHOUT_SUBSCRIPTION - 1) {
                        car.copy(
                            shouldBeBlurred = true
                        )
                    } else car
                }
            }
        }
    }

    override suspend fun saveCar(car: CarModel) {
        interactor.saveCar(car)
        if (!billingRepository.isSubscribed(BillingRepository.Products.SUBSCRIPTION)) {
            carsRepositoryDecorator.addToCount()
        }
    }

    override fun isAllowedToSaveCar(): Boolean {
        return if (billingRepository.isSubscribed(BillingRepository.Products.SUBSCRIPTION)) true
        else carsRepositoryDecorator.getCarsSavedByUserCount() < AppConfig.ADD_CAR_LIMIT
    }

    override fun isSubscribed(): Boolean {
        return billingRepository.isSubscribed(product = BillingRepository.Products.SUBSCRIPTION)
    }

    override suspend fun startSubscriptionPurchaseFlow() {
        billingRepository.sub(BillingRepository.Products.SUBSCRIPTION)
    }
}