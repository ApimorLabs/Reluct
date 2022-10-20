package work.racka.reluct.common.features.settings.states

import work.racka.reluct.common.billing.products.Product

sealed class SettingsEvents {
    object Nothing : SettingsEvents()
    class ThemeChanged(val themeValue: Int) : SettingsEvents()
    class FocusModeChanged(val isEnabled: Boolean) : SettingsEvents()
    class DndChanged(val isEnabled: Boolean) : SettingsEvents()
    class AppBlockingChanged(val isEnabled: Boolean) : SettingsEvents()
    class InitiatePurchase(val product: Product) : SettingsEvents()
}