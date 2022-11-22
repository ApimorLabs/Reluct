package work.racka.reluct.android.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.billing.revenuecat.PurchaseAction
import work.racka.reluct.common.features.settings.AppSettingsViewModel
import work.racka.reluct.common.features.settings.states.SettingsEvents
import work.racka.reluct.common.model.util.Resource

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun SettingsScreen(
    goBack: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val viewModel: AppSettingsViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = SettingsEvents.Nothing)

    val purchaseAction = remember(viewModel) {
        PurchaseAction(
            onSuccess = { viewModel.handlePurchaseResult(Resource.Success(true)) },
            onError = { msg, _ -> viewModel.handlePurchaseResult(Resource.Error(msg)) }
        )
    }

    val context = LocalContext.current
    HandleEvents(
        eventsState = events,
        context = context,
        snackbarHostState = snackbarHostState,
        purchaseAction = purchaseAction
    )

    SettingsUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onSaveTheme = viewModel::saveThemeSettings,
        onToggleDnd = viewModel::toggleDnd,
        onToggleFocusMode = viewModel::toggleFocusMode,
        onToggleAppBlocking = viewModel::toggleAppBlocking,
        onGetCoffeeProducts = viewModel::getCoffeeProducts,
        onPurchaseCoffee = viewModel::purchaseCoffee,
        onBackClicked = goBack
    )
}

@Composable
private fun HandleEvents(
    eventsState: State<SettingsEvents>,
    context: Context,
    snackbarHostState: SnackbarHostState,
    purchaseAction: PurchaseAction
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is SettingsEvents.ThemeChanged -> {
                val msg = context.getString(R.string.themes_changed_text)
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is SettingsEvents.DndChanged -> {
                val msg = if (events.isEnabled) {
                    context.getString(R.string.dnd_on_msg)
                } else {
                    context.getString(R.string.dnd_off_msg)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is SettingsEvents.FocusModeChanged -> {
                val msg = if (events.isEnabled) {
                    context.getString(R.string.focus_mode_on_msg)
                } else {
                    context.getString(R.string.focus_mode_off_msg)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is SettingsEvents.AppBlockingChanged -> {
                val msg = if (events.isEnabled) {
                    context.getString(R.string.app_blocking_turned_on_text)
                } else {
                    context.getString(R.string.app_blocking_turned_off_text)
                }
                launch {
                    snackbarHostState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is SettingsEvents.InitiatePurchase -> {
                // This will always be an Activity. If it isn't then we have bigger problems
                val activity = context as Activity
                purchaseAction.initiate(activity = activity, item = events.product.productInfo.info)
            }
            else -> {}
        }
    }
}
