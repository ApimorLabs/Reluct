package work.racka.reluct.common.billing.api

import com.revenuecat.purchases.*
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import com.revenuecat.purchases.interfaces.ReceiveOfferingsCallback
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.billing.products.ProductOffered
import work.racka.reluct.common.billing.revenuecat.asProduct
import work.racka.reluct.common.model.util.Resource
import kotlin.coroutines.resume

internal class RevenueCatAndroid(
    private val purchases: Purchases,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BillingApi {
    override suspend fun getProducts(filterProducts: List<ProductOffered>): Resource<ImmutableList<Product>> =
        withContext(dispatcher) {
            suspendCancellableCoroutine { cont ->
                val listener = object : ReceiveOfferingsCallback {
                    override fun onError(error: PurchasesError) {
                        cont.resume(Resource.Error(message = error.message))
                    }

                    override fun onReceived(offerings: Offerings) {
                        val currentOfferings = offerings.current
                        val data = if (filterProducts.isEmpty()) {
                            currentOfferings?.availablePackages?.map { p: Package ->
                                p.asProduct()
                            }?.toImmutableList() ?: persistentListOf()
                        } else {
                            filterProducts.mapNotNull { item ->
                                try {
                                    currentOfferings?.getPackage(item.id)?.asProduct()
                                } catch (e: Exception) {
                                    println(e.message)
                                    null
                                }
                            }.toImmutableList()
                        }
                        cont.resume(Resource.Success(data))
                    }
                }
                purchases.getOfferings(listener = listener)
            }
        }

    // Use PurchaseAction.kt found in revenue_cat package when launching a purchase to prevent
    // leaking Activities in business logic
    override suspend fun purchaseProduct(item: Product): Resource<Product> {
        return Resource.Success(item)
    }

    override suspend fun updatePermission(): Resource<Boolean> = withContext(dispatcher) {
        suspendCancellableCoroutine { cont ->
            val listener = object : ReceiveCustomerInfoCallback {
                override fun onError(error: PurchasesError) {
                    cont.resume(Resource.Error(message = error.message))
                }

                override fun onReceived(customerInfo: CustomerInfo) {
                    // TODO: Return the Entitlements
                    cont.resume(Resource.Success(true))
                }
            }
            purchases.syncPurchases()
            purchases.getCustomerInfo(callback = listener)
        }
    }
}
