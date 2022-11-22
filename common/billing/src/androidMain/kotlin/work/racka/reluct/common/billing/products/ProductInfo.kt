package work.racka.reluct.common.billing.products

import com.revenuecat.purchases.Package

actual class ProductInfo(val info: Package) {
    override fun equals(other: Any?): Boolean {
        return other?.let { item ->
            item is ProductInfo && item.info == this.info
        } ?: false
    }

    override fun hashCode(): Int {
        return info.hashCode()
    }
}
