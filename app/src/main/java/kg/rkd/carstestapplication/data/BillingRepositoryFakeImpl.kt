package kg.rkd.carstestapplication.data

import kg.rkd.carstestapplication.utils.AppPreferences

class BillingRepositoryFakeImpl(
    private val prefs: AppPreferences
) : BillingRepository {

    override fun isSubscribed(product: BillingRepository.Products): Boolean =
        prefs.getBoolean(product.sku)

    override fun isBougth(product: BillingRepository.Products): Boolean =
        prefs.getBoolean(product.sku)

    override fun sub(product: BillingRepository.Products) {
        prefs.set(product.sku, true)
    }

    override fun buy(product: BillingRepository.Products) {
        prefs.set(product.sku, true)
    }
}