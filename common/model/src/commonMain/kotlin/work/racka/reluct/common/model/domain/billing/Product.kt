package domain.billing

import enums.billing.ProductsOffered
import enums.billing.Sku

data class Product(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val identifier: String = "",
    var accessTo: ProductsOffered? = null,
    val type: Sku
)
