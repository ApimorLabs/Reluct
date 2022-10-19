package work.racka.reluct.android.screens.settings

import android.app.Activity
import android.content.Context
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.billing.revenue_cat.PurchaseAction
import work.racka.reluct.common.features.settings.AppSettingsViewModel
import work.racka.reluct.common.features.settings.states.SettingsEvents
import work.racka.reluct.common.model.util.Resource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScreen(
    goBack: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    val viewModel: AppSettingsViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = SettingsEvents.Nothing)

    val purchaseAction = remember(viewModel) {
        PurchaseAction(
            onSuccess = { viewModel.handlePurchaseResult(Resource.Success(true)) },
            onError = { msg, _ -> viewModel.handlePurchaseResult(Resource.Error(msg)) }
        )
    }

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            events = events,
            context = context,
            scaffoldState = scaffoldState,
            scope = this,
            purchaseAction = purchaseAction
        )
    }

    SettingsUI(
        scaffoldState = scaffoldState,
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

@OptIn(ExperimentalMaterialApi::class)
private fun handleEvents(
    events: SettingsEvents,
    context: Context,
    scaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope,
    purchaseAction: PurchaseAction
) {
    when (events) {
        is SettingsEvents.ThemeChanged -> {
            val msg = context.getString(R.string.themes_changed_text)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is SettingsEvents.DndChanged -> {
            val msg = if (events.isEnabled) context.getString(R.string.dnd_on_msg)
            else context.getString(R.string.dnd_off_msg)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is SettingsEvents.FocusModeChanged -> {
            val msg = if (events.isEnabled) context.getString(R.string.focus_mode_on_msg)
            else context.getString(R.string.focus_mode_off_msg)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is SettingsEvents.AppBlockingChanged -> {
            val msg = if (events.isEnabled) context.getString(R.string.app_blocking_turned_on_text)
            else context.getString(R.string.app_blocking_turned_off_text)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
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