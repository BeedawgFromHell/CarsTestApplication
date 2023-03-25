package kg.rkd.carstestapplication.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import kg.rkd.carstestapplication.AppConfig
import kg.rkd.carstestapplication.utils.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class FakeBillingRepositoryImpl(
    private val dataStore: AppDataStore,
) : BillingRepository {

    override fun isSubscribedAsFlow(product: BillingRepository.Products): Flow<Boolean> {
        return dataStore.getAsFlow(stringPreferencesKey(product.sku)).map { it.toBoolean() }
    }

    override fun isSubscribed(product: BillingRepository.Products): Boolean {
        return runBlocking {
            dataStore.getAsFlow(stringPreferencesKey(product.sku)).first().toBoolean()
        }
    }

    override suspend fun sub(product: BillingRepository.Products) {
        dataStore.set(stringPreferencesKey(product.sku), true.toString())
    }

    override suspend fun unSub(product: BillingRepository.Products) {
        dataStore.set(stringPreferencesKey(product.sku), false.toString())
    }
}

class FakeBillingRepositorySubDurationDecorator(
    private val billingRepository: BillingRepository,
    private val context: Context,
) : BillingRepository by billingRepository {

    override suspend fun sub(product: BillingRepository.Products) {
        billingRepository.sub(product)

        if(product == BillingRepository.Products.SUBSCRIPTION) {
            val workRequest = OneTimeWorkRequestBuilder<UnSubWorker>()
                .setInitialDelay(AppConfig.SUBSCRIPTION_DURATION.toLong(), TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}

