package work.racka.reluct.android.screens.screentime.statistics

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.buttons.ScrollToTop
import work.racka.reluct.android.compose.components.cards.appUsageEntry.AppUsageEntry
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.permissions.PermissionsCard
import work.racka.reluct.android.compose.components.cards.statistics.BarChartDefaults
import work.racka.reluct.android.compose.components.cards.statistics.screenTime.ScreenTimeStatisticsCard
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.*
import work.racka.reluct.android.screens.util.*
import work.racka.reluct.common.features.screenTime.statistics.states.allStats.DailyUsageStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.allStats.ScreenTimeStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.allStats.WeeklyUsageStatsState
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun ScreenTimeStatisticsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarHostState: SnackbarHostState,
    uiState: State<ScreenTimeStatsState>,
    getUsageData: (isGranted: Boolean) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onSaveAppTimeLimitSettings: (hours: Int, minutes: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    // Screen Time Chart
    val barColor = BarChartDefaults.barColor
    val screenTimeChartData = getWeeklyDeviceScreenTimeChartData(
        weeklyStatsProvider = { uiState.value.weeklyData.usageStats },
        isLoadingProvider = { uiState.value.weeklyData is WeeklyUsageStatsState.Loading },
        barColor = barColor
    )

    val usagePermissionGranted = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val showAppTimeLimitDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    PermissionCheckHandler {
        if (!usagePermissionGranted.value) {
            usagePermissionGranted.value = checkUsageAccessPermissions(context)
            getUsageData(usagePermissionGranted.value)
        }
    }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = snackbarModifier.value,
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
                if (!usagePermissionGranted.value) {
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
                        chartData = screenTimeChartData,
                        selectedDayText = { uiState.value.dailyData.dayText },
                        selectedDayScreenTime = { uiState.value.dailyData.usageStat.formattedTotalScreenTime },
                        weeklyTotalScreenTime = { uiState.value.weeklyData.formattedTotalTime },
                        selectedDayIsoNumber = { uiState.value.selectedInfo.selectedDay },
                        onBarClicked = { onSelectDay(it) },
                        weekUpdateButton = {
                            ScreenTimeWeekSelectorButton(
                                selectedInfoProvider = { uiState.value.selectedInfo },
                                onUpdateWeekOffset = onUpdateWeekOffset
                            )
                        }
                    )
                }

                // Apps Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.app_list_header))
                }

                dailyAppStatsList(
                    dailyDataProvider = { uiState.value.dailyData},
                    isLoadingProvider = { uiState.value.dailyData is DailyUsageStatsState.Loading},
                    onAppUsageInfoClick = onAppUsageInfoClick,
                    onAppTimeLimitSettingsClicked = onAppTimeLimitSettingsClicked,
                    onShowLimitDialog = { showAppTimeLimitDialog.value = true }
                )

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }

        // Go To Usage Access Dialog
        UsagePermissionDialog(openDialog = openDialog, onClose = { openDialog.value = false })

        // App Time Limit Dialog
        ShowAppTimeLimitDialog(
            openDialog = showAppTimeLimitDialog,
            limitStateProvider = { uiState.value.appTimeLimit },
            onSaveTimeLimit = onSaveAppTimeLimitSettings,
            onClose = { showAppTimeLimitDialog.value = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.dailyAppStatsList(
    dailyDataProvider: () -> DailyUsageStatsState,
    isLoadingProvider: () -> Boolean,
    onAppUsageInfoClick: (app: AppUsageInfo) -> Unit,
    onAppTimeLimitSettingsClicked: (packageName: String) -> Unit,
    onShowLimitDialog: () -> Unit
) {
    // Daily Data Loading
    item {
        val isLoading = remember { derivedStateOf { isLoadingProvider() } }
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = isLoading.value,
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
    item {
        val isEmpty = remember {
            derivedStateOf { dailyDataProvider() is DailyUsageStatsState.Empty }
        }
        AnimatedVisibility(
            visible = isEmpty.value,
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

    items(
        items = dailyDataProvider().usageStat.appsUsageList,
        key = { it.packageName }
    ) { item ->
        AppUsageEntry(
            playAnimation = true,
            modifier = Modifier.animateItemPlacement(),
            appUsageInfo = item,
            onEntryClick = { onAppUsageInfoClick(item) },
            onTimeSettingsClick = {
                onAppTimeLimitSettingsClicked(item.packageName)
                onShowLimitDialog()
            }
        )
    }
}
