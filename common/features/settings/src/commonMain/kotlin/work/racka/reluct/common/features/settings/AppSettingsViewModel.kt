package work.racka.reluct.common.features.settings

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.domain.usecases.billing.ManageCoffeeProducts
import work.racka.reluct.common.features.screen_time.services.ScreenTimeServices
import work.racka.reluct.common.features.settings.states.CoffeeProductsState
import work.racka.reluct.common.features.settings.states.LimitSettings
import work.racka.reluct.common.features.settings.states.SettingsEvents
import work.racka.reluct.common.features.settings.states.SettingsState
import work.racka.reluct.common.model.util.Resource
import work.racka.reluct.common.settings.MultiplatformSettings

class AppSettingsViewModel(
    private val settings: MultiplatformSettings,
    private val screenTimeServices: ScreenTimeServices,
    private val manageCoffeeProducts: ManageCoffeeProducts
) : CommonViewModel() {

    private val limitSettings = combine(
        settings.doNoDisturb,
        settings.focusMode,
        settings.appBlockingEnabled
    ) { dnd, focusMode, appBlocking ->
        LimitSettings(
            dndOn = dnd,
            focusModeOn = focusMode,
            appBlockingEnabled = appBlocking
        )
    }

    private val coffeeProductsState: MutableStateFlow<CoffeeProductsState> =
        MutableStateFlow(CoffeeProductsState.Loading)

    val uiState: StateFlow<SettingsState> = combine(
        settings.theme, limitSettings, coffeeProductsState
    ) { themeSelected, limitSettings, coffeeProducts ->
        SettingsState(
            themeValue = themeSelected,
            limitSettings = limitSettings,
            coffeeProducts = coffeeProducts
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsState()
    )

    private val eventsChannel = Channel<SettingsEvents>()
    val events: Flow<SettingsEvents> = eventsChannel.receiveAsFlow()

    private var coffeeJob: Job? = null

    private var currentProduct: Product? = null

    fun saveThemeSettings(themeValue: Int) {
        val saved = settings.saveThemeSettings(themeValue)
        if (saved) {
            eventsChannel.trySend(SettingsEvents.ThemeChanged(themeValue))
        }
    }

    fun toggleDnd(value: Boolean) {
        vmScope.launch {
            settings.saveDoNotDisturb(value)
            eventsChannel.send(SettingsEvents.DndChanged(value))
        }
    }

    fun toggleFocusMode(value: Boolean) {
        vmScope.launch {
            settings.saveFocusMode(value)
            eventsChannel.send(SettingsEvents.FocusModeChanged(value))
        }
    }

    fun toggleAppBlocking(value: Boolean) {
        vmScope.launch {
            if (settings.saveAppBlocking(value)) {
                eventsChannel.send(SettingsEvents.AppBlockingChanged(value))
                if (value) screenTimeServices.startLimitsService()
                else screenTimeServices.stopLimitsService()
            }
        }
    }

    fun getCoffeeProducts() {
        coffeeJob?.cancel()
        coffeeJob = vmScope.launch {
            coffeeProductsState.update { CoffeeProductsState.Loading }
            when (val result = manageCoffeeProducts.getAllProducts()) {
                is Resource.Success -> {
                    result.data?.let { products ->
                        coffeeProductsState.update { CoffeeProductsState.ShowProducts(products) }
                    } ?: coffeeProductsState.update { CoffeeProductsState.ShowProducts(listOf()) }
                }
                is Resource.Error -> {
                    val message = result.message ?: "Error Occurred"
                    coffeeProductsState.update { CoffeeProductsState.FetchError(message) }
                }
                else -> {}
            }
        }
    }

    fun purchaseCoffee(product: Product) {
        vmScope.launch {
            currentProduct = product
            // TODO: Should be revamped when checking on non Android platforms
            manageCoffeeProducts.api.purchaseProduct(product)
            eventsChannel.send(SettingsEvents.InitiatePurchase(product))
            coffeeProductsState.update { CoffeeProductsState.Loading }
        }
    }

    // TODO: Check alternatives later
    fun managePurchaseResult(result: Resource<Boolean>) {
        vmScope.launch {
            when (result) {
                is Resource.Success -> {
                    currentProduct?.let { product ->
                        coffeeProductsState.update { CoffeeProductsState.PurchaseSuccess(product) }
                    }
                        ?: coffeeProductsState
                            .update { CoffeeProductsState.FetchError("Internal Error") }
                    manageCoffeeProducts.api.updatePermission() // Update Entitlements
                }
                is Resource.Error -> {
                    currentProduct?.let { product ->
                        coffeeProductsState.update {
                            CoffeeProductsState.PurchaseError(
                                product = product,
                                message = result.message ?: "Error Occurred"
                            )
                        }
                    }
                        ?: coffeeProductsState
                            .update { CoffeeProductsState.FetchError("Internal Error") }
                }
                else -> coffeeProductsState
                    .update { CoffeeProductsState.FetchError("Internal Error") }
            }
            currentProduct = null
        }
    }
}