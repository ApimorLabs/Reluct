package work.racka.reluct.common.billing.api

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.billing.products.Product

interface BillingApi {
    fun getProducts(): Flow<List<Product>>
    fun purchaseProduct(item: Product)
    fun updatePermission(): Flow<Product?>
}