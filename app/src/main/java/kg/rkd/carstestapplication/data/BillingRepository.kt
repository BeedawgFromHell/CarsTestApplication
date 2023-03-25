package kg.rkd.carstestapplication.data

import kotlinx.coroutines.flow.Flow

interface BillingRepository {
    fun isSubscribedAsFlow(product: Products): Flow<Boolean>
    fun isSubscribed(product: Products): Boolean
    suspend fun sub(product: Products)

    enum class Products(val sku: String) {
        SUBSCRIPTION("asdasdasd")
    }
}