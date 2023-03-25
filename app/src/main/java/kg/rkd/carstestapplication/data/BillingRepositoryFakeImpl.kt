package kg.rkd.carstestapplication.data

import androidx.datastore.preferences.core.stringPreferencesKey
import kg.rkd.carstestapplication.utils.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class BillingRepositoryFakeImpl(
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