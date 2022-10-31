package work.racka.reluct.common.domain.usecases.billing

import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.common.billing.api.BillingApi
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.model.util.Resource

interface ManageCoffeeProducts {
    val api: BillingApi // Direct access to the API
    suspend fun getAllProducts(): Resource<ImmutableList<Product>>
}