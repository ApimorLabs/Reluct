package domain

import billing.ProductsOffered
import billing.Sku

data class Product(
    val title: String = "",
    val description: String = "",
    val price: String = "",
    val identifier: String = "",
    var accessTo: ProductsOffered? = null,
    val type: Sku
)
