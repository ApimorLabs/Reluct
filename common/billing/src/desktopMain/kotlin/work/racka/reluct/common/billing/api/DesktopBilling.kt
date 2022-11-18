package work.racka.reluct.common.billing.api

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.billing.products.ProductOffered
import work.racka.reluct.common.model.util.Resource

internal class DesktopBilling : BillingApi {
    override suspend fun getProducts(filterProducts: List<ProductOffered>): Resource<ImmutableList<Product>> {
        return Resource.Success(persistentListOf())
    }

    override suspend fun purchaseProduct(item: Product): Resource<Product> {
        return Resource.Error("Not Implemented")
    }

    override suspend fun updatePermission(): Resource<Boolean> {
        return Resource.Success(true)
    }
}