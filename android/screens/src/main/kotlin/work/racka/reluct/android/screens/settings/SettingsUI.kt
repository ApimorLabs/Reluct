package work.racka.reluct.android.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.cards.cardWithActions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.android.screens.settings.components.AppAboutInfo
import work.racka.reluct.android.screens.settings.components.CoffeeProductsSheet
import work.racka.reluct.android.screens.settings.components.ThemesDialog
import work.racka.reluct.common.billing.products.Product
import work.racka.reluct.common.features.settings.states.SettingsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun SettingsUI(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    uiState: SettingsState,
    onSaveTheme: (value: Int) -> Unit,
    onToggleDnd: (value: Boolean) -> Unit,
    onToggleFocusMode: (value: Boolean) -> Unit,
    onToggleAppBlocking: (value: Boolean) -> Unit,
    onGetCoffeeProducts: () -> Unit,
    onPurchaseCoffee: (Product) -> Unit,
    onBackClicked: () -> Unit
) {

    var openThemeDialog by remember { mutableStateOf(false) }

    val modalSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            CoffeeProductsSheet(
                modifier = Modifier.statusBarsPadding(),
                state = uiState.coffeeProducts,
                onPurchaseProduct = onPurchaseCoffee,
                onReloadProducts = onGetCoffeeProducts,
                onClose = {
                    scope.launch {
                        modalSheetState.hide()
                    }
                }
            )
        },
        sheetElevation = 0.dp,
        sheetBackgroundColor = Color.Transparent
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ReluctSmallTopAppBar(
                    modifier = Modifier.statusBarsPadding(),
                    title = stringResource(R.string.settings_text),
                    navigationIcon = {
                        IconButton(onClick = onBackClicked) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        modifier = Modifier.navigationBarsPadding(),
                        snackbarData = it,
                        shape = RoundedCornerShape(10.dp),
                        containerColor = MaterialTheme.colorScheme.inverseSurface,
                        contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        actionColor = MaterialTheme.colorScheme.primary,
                    )
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(Dimens.MediumPadding.size),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Dimens.MediumPadding.size)
            ) {
                // Theme Settings
                item {
                    ReluctDescriptionCard(
                        title = {
                            Text(
                                text = stringResource(id = R.string.themes_text),
                                style = MaterialTheme.typography.titleLarge,
                                color = LocalContentColor.current
                            )
                        },
                        description = {
                            Text(
                                text = stringResource(id = R.string.themes_desc_text),
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalContentColor.current.copy(alpha = .8f)
                            )
                        },
                        leftItems = {
                            Icon(
                                imageVector = Icons.Rounded.DarkMode,
                                contentDescription = null
                            )
                        },
                        rightItems = {
                            Icon(
                                imageVector = Icons.Rounded.ChevronRight,
                                contentDescription = "Open"
                            )
                        },
                        onClick = { openThemeDialog = true }
                    )
                }

                // Turn On Focus Mode
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.turn_on_focus),
                        description = stringResource(R.string.turn_on_focus_desc),
                        checked = uiState.limitSettings.focusModeOn,
                        onCheckedChange = onToggleFocusMode,
                        icon = Icons.Rounded.AppShortcut
                    )
                }

                // App Blocking
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.app_blocking_text),
                        description = stringResource(R.string.app_blocking_desc_text),
                        checked = uiState.limitSettings.appBlockingEnabled,
                        onCheckedChange = onToggleAppBlocking,
                        icon = Icons.Rounded.AppBlocking
                    )
                }

                // Enable DND
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.turn_on_dnd),
                        description = stringResource(R.string.turn_on_dnd_desc),
                        checked = uiState.limitSettings.dndOn,
                        onCheckedChange = onToggleDnd,
                        icon = Icons.Rounded.DoNotDisturbOnTotalSilence
                    )
                }

                // Support Development
                item {
                    LimitsDetailsCard(
                        title = stringResource(id = R.string.support_development_text),
                        description = stringResource(id = R.string.support_development_desc_text),
                        icon = Icons.Rounded.Favorite,
                        onClick = {
                            scope.launch {
                                onGetCoffeeProducts()
                                modalSheetState.show()
                            }
                        }
                    )
                }

                // About Details
                item {
                    AppAboutInfo()
                }

                // Bottom Space
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }

    if (openThemeDialog) {
        ThemesDialog(
            onDismiss = { openThemeDialog = false },
            currentTheme = uiState.themeValue,
            onSaveTheme = onSaveTheme
        )
    }
}