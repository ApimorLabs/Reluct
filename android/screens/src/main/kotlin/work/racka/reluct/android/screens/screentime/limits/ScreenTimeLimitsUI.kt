package work.racka.reluct.android.screens.screentime.limits

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.AppNameEntry
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.common.features.screen_time.limits.states.ScreenTimeLimitState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScreenTimeLimitsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    scaffoldState: ScaffoldState,
    uiState: ScreenTimeLimitState,
    toggleFocusMode: (Boolean) -> Unit,
    toggleDnd: (Boolean) -> Unit,
    getDistractingApps: () -> Unit,
    pauseApp: (packageName: String) -> Unit,
    resumeApp: (packageName: String) -> Unit,
    makeDistractingApp: (packageName: String) -> Unit,
    removeDistractingApp: (packageName: String) -> Unit,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    // Need to evaluate recomposition overhead when user it at the
    // top of the list
    if (scrollContext.isTop) {
        barsVisibility.bottomBar.show()
    } else {
        barsVisibility.bottomBar.hide()
    }

    val snackbarModifier = if (scrollContext.isTop) {
        Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
    } else Modifier.navigationBarsPadding()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = snackbarModifier,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
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
                        icon = Icons.Rounded.AppBlocking
                    )
                }

                // Enable DND
                item {
                    LimitsSwitchCard(
                        title = stringResource(R.string.turn_on_dnd),
                        description = stringResource(R.string.turn_on_dnd_desc),
                        checked = uiState.focusModeState.doNotDisturbOn,
                        onCheckedChange = { toggleDnd(it) }
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
                        onClick = {}
                    )
                }

                // Manually Pause Apps
                item {
                    LimitsDetailsCard(
                        title = stringResource(R.string.manually_pause_apps),
                        description = stringResource(R.string.manage_distracting_apps_desc),
                        onClick = {}
                    ) {
                        // Bottom Content
                        Divider(
                            modifier = Modifier.padding(
                                vertical = Dimens.SmallPadding.size,
                                horizontal = Dimens.MediumPadding.size
                            )
                        )
                        uiState.pausedAppsState.pausedApps.take(3).forEach { app ->
                            AppNameEntry(
                                modifier = Modifier.padding(horizontal = Dimens.MediumPadding.size),
                                appName = app.appName,
                                icon = app.appIcon.icon
                            )
                        }
                    }
                }
            }
        }
    }
}