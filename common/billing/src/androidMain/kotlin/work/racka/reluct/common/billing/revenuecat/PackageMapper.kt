package work.racka.reluct.common.billing.revenuecat

import com.revenuecat.purchases.Package
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.billing.products.ProductInfo
import work.racka.reluct.common.billing.products.ProductOffered

internal fun Package.asProduct() = Product(
    name = this.product.title,
    description = product.description,
    price = product.price,
    currencyCode = product.priceCurrencyCode,
    productInfo = ProductInfo(this),
    productOffered = ProductOffered(
        sku = packageType.getSku(),
        id = product.sku
    )
)
