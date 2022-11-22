package work.racka.reluct.common.domain.usecases.billing.impl

import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.common.billing.api.BillingApi
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.billing.products.Products
import work.racka.reluct.common.domain.usecases.billing.ManageCoffeeProducts
import work.racka.reluct.common.model.util.Resource

internal class ManageCoffeeProductsImpl(
    billingApi: BillingApi
) : ManageCoffeeProducts {
    override val api: BillingApi = billingApi

    override suspend fun getAllProducts(): Resource<ImmutableList<Product>> {
        val products = listOf(Products.coffee1, Products.coffee2, Products.coffee3)
        return api.getProducts(filterProducts = products)
    }
}
