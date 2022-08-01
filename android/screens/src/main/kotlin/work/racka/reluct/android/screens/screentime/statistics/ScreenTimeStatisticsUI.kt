package work.racka.reluct.android.screens.screentime.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.android.compose.components.cards.app_usage_entry.AppUsageEntry
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.permissions.PermissionsCard
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.screen_time.ScreenTimeStatisticsCard
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.AppTimeLimitDialog
import work.racka.reluct.android.screens.util.PermissionCheckHandler
import work.racka.reluct.android.screens.util.checkUsageAccessPermissions
import work.racka.reluct.android.screens.util.requestUsageAccessPermission
import work.racka.reluct.common.features.screen_time.statistics.states.DailyUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.ScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.WeeklyUsageStatsState
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ScreenTimeStatisticsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    scaffoldState: ScaffoldState,
    uiState: ScreenTimeStatsState,
    getUsageData: (isGranted: Boolean) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onSaveAppTimeLimitSettings: (hours: Int, minutes: Int) -> Unit
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

    val barChartState = remember(uiState.weeklyData) {
        derivedStateOf {
            val result = when (val weeklyState = uiState.weeklyData) {
                is WeeklyUsageStatsState.Data -> {
                    StatisticsChartState.Success(data = weeklyState.usageStats)
                }
                is WeeklyUsageStatsState.Loading -> {
                    StatisticsChartState.Loading(data = weeklyState.usageStats)
                }
                is WeeklyUsageStatsState.Empty -> {
                    StatisticsChartState.Empty(data = weeklyState.usageStats)
                }
                else -> {
                    StatisticsChartState.Empty(data = weeklyState.usageStats)
                }
            }
            result
        }
    }

    var usagePermissionGranted by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    var showAppTimeLimitDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    PermissionCheckHandler {
        if (!usagePermissionGranted) {
            usagePermissionGranted = checkUsageAccessPermissions(context)
            getUsageData(usagePermissionGranted)
        }
    }

    //LaunchedEffect(usagePermissionGranted) { getUsageData() }

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
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.SmallPadding.size)
            ) {
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }

                // Permission Card
                if (!usagePermissionGranted) {
                    item {
                        PermissionsCard(
                            imageSlot = {
                                LottieAnimationWithDescription(
                                    iterations = Int.MAX_VALUE,
                                    lottieResId = R.raw.no_permission,
                                    imageSize = 200.dp,
                                    description = null
                                )
                            },
                            permissionDetails = stringResource(R.string.usage_permissions_details),
                            onPermissionRequest = { openDialog.value = true }
                        )
                    }
                }

                // Chart
                item {
                    ScreenTimeStatisticsCard(
                        barChartState = barChartState.value,
                        selectedDayText = uiState.dailyData.dayText,
                        selectedDayScreenTime = uiState.dailyData.usageStat.formattedTotalScreenTime,
                        weeklyTotalScreenTime = uiState.weeklyData.formattedTotalTime,
                        selectedDayIsoNumber = uiState.selectedInfo.selectedDay,
                        onBarClicked = { onSelectDay(it) }
                    ) {
                        ValueOffsetButton(
                            text = uiState.selectedInfo.selectedWeekText,
                            offsetValue = uiState.selectedInfo.weekOffset,
                            onOffsetValueChange = { onUpdateWeekOffset(it) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = Shapes.large,
                            incrementEnabled = uiState.selectedInfo.weekOffset < 0
                        )
                    }
                }

                // Apps Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.app_list_header))
                }

                // Daily Data Loading
                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = uiState.dailyData is DailyUsageStatsState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            LinearProgressIndicator()
                        }
                    }
                }

                // No App Data Animation
                if (uiState.dailyData is DailyUsageStatsState.Empty) {
                    item {
                        AnimatedVisibility(
                            visible = uiState.dailyData is DailyUsageStatsState.Empty,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                LottieAnimationWithDescription(
                                    iterations = 10,
                                    lottieResId = R.raw.no_data,
                                    imageSize = 200.dp,
                                    description = stringResource(R.string.no_usage_data_text)
                                )
                            }
                        }
                    }
                }

                if (uiState.dailyData.usageStat.appsUsageList.isNotEmpty()) {
                    items(
                        items = uiState.dailyData.usageStat.appsUsageList,
                        key = { it.packageName }
                    ) { item ->
                        AppUsageEntry(
                            playAnimation = true,
                            appUsageInfo = item,
                            onEntryClick = { onAppUsageInfoClick(item) },
                            onTimeSettingsClick = {
                                onAppTimeLimitSettingsClicked(item.packageName)
                                showAppTimeLimitDialog = true
                            }
                        )
                    }
                }

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier
                            .padding(bottom = Dimens.ExtraLargePadding.size)
                            .navigationBarsPadding()
                    )
                }
            }
        }


        // Go To Usage Access Dialog
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = {
                    Text(text = stringResource(R.string.open_settings_dialog_title))
                },
                text = {
                    Text(text = stringResource(R.string.usage_permissions_rationale_dialog_text))
                },
                confirmButton = {
                    ReluctButton(
                        buttonText = stringResource(R.string.ok),
                        icon = null,
                        shape = Shapes.large,
                        buttonColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onButtonClicked = {
                            openDialog.value = false
                            requestUsageAccessPermission(context)
                        }
                    )
                },
                dismissButton = {
                    ReluctButton(
                        buttonText = stringResource(R.string.cancel),
                        icon = null,
                        shape = Shapes.large,
                        buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        onButtonClicked = { openDialog.value = false }
                    )
                }
            )
        }

        // App Time Limit Dialog
        if (showAppTimeLimitDialog) {
            AppTimeLimitDialog(
                onDismiss = { showAppTimeLimitDialog = false },
                appTimeLimitState = uiState.appTimeLimit,
                onSaveTimeLimit = onSaveAppTimeLimitSettings
            )
        }
    }
}