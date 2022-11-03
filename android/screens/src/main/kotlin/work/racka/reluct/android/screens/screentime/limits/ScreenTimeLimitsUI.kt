package work.racka.reluct.android.screens.screentime.limits

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppShortcut
import androidx.compose.material.icons.rounded.DoNotDisturbOnTotalSilence
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.AppNameEntry
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.android.screens.screentime.components.ManageAppsDialog
import work.racka.reluct.common.features.screenTime.limits.states.DistractingAppsState
import work.racka.reluct.common.features.screenTime.limits.states.PausedAppsState
import work.racka.reluct.common.features.screenTime.limits.states.ScreenTimeLimitState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ScreenTimeLimitsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: ScreenTimeLimitState,
    toggleFocusMode: (Boolean) -> Unit,
    toggleDnd: (Boolean) -> Unit,
    getDistractingApps: () -> Unit,
    pauseApp: (packageName: String) -> Unit,
    resumeApp: (packageName: String) -> Unit,
    makeDistractingApp: (packageName: String) -> Unit,
    removeDistractingApp: (packageName: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext by rememberScrollContext(listState = listState)

    SideEffect {
        if (scrollContext.isTop) {
            barsVisibility.bottomBar.show()
        } else {
            barsVisibility.bottomBar.hide()
        }
    }

    val focusModeContainerColor by animateColorAsState(
        targetValue = if (uiState.focusModeState.focusModeOn) {
            Color.Green.copy(alpha = .7f)
        } else {
            MaterialTheme.colorScheme.error.copy(alpha = .8f)
        }
    )

    val focusModeContentColor by animateColorAsState(
        targetValue = if (uiState.focusModeState.focusModeOn) {
            Color.Black
        } else {
            MaterialTheme.colorScheme.onError
        }
    )

    // Dialogs Open State
    val showDistractingAppsDialog = remember { mutableStateOf(false) }
    val showPausedAppsDialog = remember { mutableStateOf(false) }

    val snackbarModifier = if (scrollContext.isTop) {
        Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
    } else {
        Modifier.navigationBarsPadding()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = snackbarModifier,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->

        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
            ) {
                // Focus Mode Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.focus_mode_header))
                }

                // Turn On Focus Mode
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.turn_on_focus),
                        description = stringResource(R.string.turn_on_focus_desc),
                        checked = uiState.focusModeState.focusModeOn,
                        onCheckedChange = { toggleFocusMode(it) },
                        icon = Icons.Rounded.AppShortcut,
                        containerColor = focusModeContainerColor,
                        contentColor = focusModeContentColor
                    )
                }

                // Enable DND
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.turn_on_dnd),
                        description = stringResource(R.string.turn_on_dnd_desc),
                        checked = uiState.focusModeState.doNotDisturbOn,
                        onCheckedChange = { toggleDnd(it) },
                        icon = Icons.Rounded.DoNotDisturbOnTotalSilence
                    )
                }

                // Distractions Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.distractions_header))
                }

                // Manage Distracting Apps
                item {
                    LimitsDetailsCard(
                        title = stringResource(R.string.manage_distracting_apps),
                        description = stringResource(R.string.manage_distracting_apps_desc),
                        onClick = {
                            getDistractingApps()
                            showDistractingAppsDialog.value = true
                        }
                    )
                }

                // Manually Pause Apps
                item {
                    LimitsDetailsCard(
                        title = stringResource(R.string.manually_pause_apps),
                        description = stringResource(R.string.manage_distracting_apps_desc),
                        onClick = { showPausedAppsDialog.value = true }
                    ) {
                        // Bottom Content
                        Divider(
                            modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        uiState.pausedAppsState.pausedApps.take(4).forEach { app ->
                            AppNameEntry(
                                modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
                                appName = app.appName,
                                icon = app.appIcon.icon
                            )
                        }
                        if (uiState.pausedAppsState is PausedAppsState.Loading) {
                            LinearProgressIndicator(
                                Modifier
                                    .fillMaxSize()
                                    .padding(Dimens.LargePadding.size)
                            )
                        } else if (uiState.pausedAppsState.pausedApps.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(Dimens.LargePadding.size),
                                text = stringResource(R.string.no_paused_apps_text),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Add Padding At the Bottom
                        Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                    }
                }

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }
        }
    }

    // All Dialogs
    if (showDistractingAppsDialog.value) {
        ManageAppsDialog(
            onDismiss = { showDistractingAppsDialog.value = false },
            isLoading = uiState.distractingAppsState is DistractingAppsState.Loading,
            topItemsHeading = stringResource(id = R.string.distracting_apps_text),
            bottomItemsHeading = stringResource(id = R.string.non_distracting_apps_text),
            topItems = uiState.distractingAppsState.distractingApps,
            bottomItems = uiState.distractingAppsState.otherApps,
            onTopItemClicked = { removeDistractingApp(it.packageName) },
            onBottomItemClicked = { makeDistractingApp(it.packageName) }
        )
    }

    if (showPausedAppsDialog.value) {
        ManageAppsDialog(
            onDismiss = { showPausedAppsDialog.value = false },
            isLoading = uiState.pausedAppsState is PausedAppsState.Loading,
            topItemsHeading = stringResource(id = R.string.paused_apps_text),
            bottomItemsHeading = stringResource(id = R.string.apps_text),
            topItems = uiState.pausedAppsState.pausedApps,
            bottomItems = uiState.pausedAppsState.unPausedApps,
            onTopItemClicked = { resumeApp(it.packageName) },
            onBottomItemClicked = { pauseApp(it.packageName) }
        )
    }
}
