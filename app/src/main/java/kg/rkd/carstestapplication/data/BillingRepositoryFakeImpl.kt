package kg.rkd.carstestapplication.data

import androidx.datastore.preferences.core.stringPreferencesKey
import kg.rkd.carstestapplication.utils.BillingDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BillingRepositoryFakeImpl(
    private val dataStore: BillingDataStore,
) : BillingRepository {

    override fun isSubscribedAsFlow(product: BillingRepository.Products): Flow<Boolean> {
        return dataStore.isBought(stringPreferencesKey(product.sku))
    }

    override fun isSubscribed(product: BillingRepository.Products): Boolean {
        return runBlocking {
            dataStore.isBought(stringPreferencesKey(product.sku)).first()
        }
    }

    override suspend fun sub(product: BillingRepository.Products) {
        dataStore.setBought(stringPreferencesKey(product.sku))
    }
}