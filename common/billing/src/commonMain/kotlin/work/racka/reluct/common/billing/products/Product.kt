package work.racka.reluct.common.billing.products

data class Product(
    val name: String,
    val description: String,
    val productOffered: ProductOffered,
    val productInfo: ProductInfo
)
