package work.racka.reluct.common.billing.products

actual class ProductInfo(val id: String) {
    override fun equals(other: Any?): Boolean {
        return other?.let { item ->
            item is ProductInfo && item.id == this.id
        } ?: false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
