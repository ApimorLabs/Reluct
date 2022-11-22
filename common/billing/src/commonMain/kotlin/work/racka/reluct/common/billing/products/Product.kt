package work.racka.reluct.common.billing.products

data class Product(
    val name: String,
    val description: String,
    val price: String,
    val currencyCode: String,
    val productOffered: ProductOffered,
    val productInfo: ProductInfo
) {

    /**
     * [ProductInfo] is a class defined as expect/actual and will not be equal
     * with the default implementation.
     */
    override fun equals(other: Any?): Boolean {
        return other?.let { otherItem ->
            otherItem is Product && otherItem.name == this.name &&
                otherItem.description == this.description &&
                otherItem.price == this.price &&
                otherItem.currencyCode == this.currencyCode &&
                otherItem.productOffered == this.productOffered &&
                otherItem.productInfo == this.productInfo
        } ?: false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
