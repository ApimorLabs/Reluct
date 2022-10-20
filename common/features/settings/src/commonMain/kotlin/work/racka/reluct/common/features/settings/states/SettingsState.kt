package work.racka.reluct.common.features.settings.states

import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.settings.Keys

data class SettingsState(
    val themeValue: Int = Keys.Defaults.THEME,
    val limitSettings: LimitSettings = LimitSettings(),
    val coffeeProducts: CoffeeProductsState = CoffeeProductsState.Loading
)

data class LimitSettings(
    val dndOn: Boolean = false,
    val focusModeOn: Boolean = false,
    val appBlockingEnabled: Boolean = false
)

sealed class CoffeeProductsState {
    object Loading : CoffeeProductsState()

    data class FetchError(val message: String) : CoffeeProductsState()

    data class ShowProducts(val products: List<Product>) : CoffeeProductsState()

    data class PurchaseSuccess(val product: Product) : CoffeeProductsState()

    data class PurchaseError(val product: Product, val message: String) : CoffeeProductsState()
}
