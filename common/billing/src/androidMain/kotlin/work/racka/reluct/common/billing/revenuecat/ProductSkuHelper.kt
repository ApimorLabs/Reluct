package work.racka.reluct.common.billing.revenuecat

import com.revenuecat.purchases.PackageType
import work.racka.reluct.common.billing.products.Sku

fun PackageType.getSku(): Sku =
    when (this) {
        PackageType.UNKNOWN -> Sku.InAppPurchase
        PackageType.CUSTOM -> Sku.InAppPurchase
        PackageType.LIFETIME -> Sku.InAppPurchase
        else -> Sku.Subscription
    }
