package work.racka.reluct.common.domain.usecases.billing

import work.racka.reluct.common.billing.api.BillingApi
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.model.util.Resource

interface ManageCoffeeProducts {
    val api: BillingApi // Direct access to the API
    suspend fun getAllProducts(): Resource<List<Product>>
}