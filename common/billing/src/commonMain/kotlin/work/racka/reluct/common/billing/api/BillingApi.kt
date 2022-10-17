package work.racka.reluct.common.billing.api

import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.billing.products.ProductOffered
import work.racka.reluct.common.model.util.Resource

interface BillingApi {
    suspend fun getProducts(filterProducts: List<ProductOffered> = listOf()): Resource<List<Product>>
    suspend fun purchaseProduct(item: Product): Resource<Product>

    /** This return type is temporary. TODO: Change this later
     * Will have to figure out the suitable return type when we add other platforms impl
     */
    suspend fun updatePermission(): Resource<Boolean>
}