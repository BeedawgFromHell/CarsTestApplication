package kg.rkd.carstestapplication.data

interface BillingRepository {
    fun isSubscribed(product: Products): Boolean
    fun isBougth(product: Products): Boolean

    fun sub(product: Products)
    fun buy(product: Products)

    enum class Products(val sku: String) {
        SUBSCRIPTION("asdasdasd")
    }
}