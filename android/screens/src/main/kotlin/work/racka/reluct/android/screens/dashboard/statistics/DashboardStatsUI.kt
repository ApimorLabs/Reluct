package work.racka.reluct.android.screens.dashboard.statistics

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.cards.app_usage_entry.AppUsageEntryBase
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.screen_time.ScreenTimeStatisticsCard
import work.racka.reluct.android.compose.components.cards.statistics.tasks.TasksStatisticsCard
import work.racka.reluct.android.compose.components.dialogs.CircularProgressDialog
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.screentime.components.AppTimeLimitDialog
import work.racka.reluct.common.features.screen_time.limits.states.AppTimeLimitState
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.ScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.all_stats.WeeklyUsageStatsState
import work.racka.reluct.common.model.domain.usagestats.AppUsageInfo
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardStatsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    screenTimeUiState: ScreenTimeStatsState,
    tasksStatsUiState: TasksStatisticsState,
    onTasksSelectDay: (day: Int) -> Unit,
    onScreenTimeSelectDay: (day: Int) -> Unit,
    onSelectAppTimeLimit: (packageName: String) -> Unit,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onAppUsageInfoClick: (appInfo: AppUsageInfo) -> Unit,
    onViewAllScreenTimeStats: () -> Unit
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    //TODO: Need to evaluate recomposition overhead when user it at the
    // top of the list
    if (scrollContext.isTop) {
        barsVisibility.bottomBar.show()
    } else {
        barsVisibility.bottomBar.hide()
    }

    // Bar Charts
    // Screen Time Chart
    val screenTimeChartState = remember(screenTimeUiState.weeklyData) {
        derivedStateOf {
            val result = when (val weeklyState = screenTimeUiState.weeklyData) {
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

    // Tasks Stats Chart
    val tasksChartState = remember(tasksStatsUiState.weeklyTasksState) {
        derivedStateOf {
            val result = when (val weeklyState = tasksStatsUiState.weeklyTasksState) {
                is WeeklyTasksState.Data -> {
                    StatisticsChartState.Success(data = weeklyState.weeklyTasks)
                }
                is WeeklyTasksState.Loading -> {
                    StatisticsChartState.Loading(data = weeklyState.weeklyTasks)
                }
                is WeeklyTasksState.Empty -> {
                    StatisticsChartState.Empty(data = weeklyState.weeklyTasks)
                }
                else -> {
                    StatisticsChartState.Empty(data = weeklyState.weeklyTasks)
                }
            }
            result
        }
    }

    var showAppTimeLimitDialog by remember { mutableStateOf(false) }

    val snackbarModifier = if (scrollContext.isTop) {
        Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
    } else Modifier.navigationBarsPadding()

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
                modifier = Modifier
                    .fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement
                    .spacedBy(Dimens.SmallPadding.size)
            ) {

                // Screen Time
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.screen_time_text))
                }

                // Screen Time Chart
                item {
                    ScreenTimeStatisticsCard(
                        barChartState = screenTimeChartState.value,
                        selectedDayText = screenTimeUiState.dailyData.dayText,
                        selectedDayScreenTime = screenTimeUiState.dailyData
                            .usageStat.formattedTotalScreenTime,
                        weeklyTotalScreenTime = screenTimeUiState.weeklyData.formattedTotalTime,
                        selectedDayIsoNumber = screenTimeUiState.selectedInfo.selectedDay,
                        onBarClicked = { onScreenTimeSelectDay(it) }
                    ) {
                        // Show 2 Apps
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            screenTimeUiState.dailyData.usageStat.appsUsageList.take(3)
                                .forEach { item ->
                                    AppUsageEntryBase(
                                        modifier = Modifier
                                            .padding(vertical = Dimens.SmallPadding.size)
                                            .fillMaxWidth()
                                            .clip(Shapes.large)
                                            .clickable { onAppUsageInfoClick(item) },
                                        appUsageInfo = item,
                                        onTimeSettingsClick = {
                                            onSelectAppTimeLimit(item.packageName)
                                            showAppTimeLimitDialog = true
                                        },
                                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                            OutlinedReluctButton(
                                modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
                                buttonText = stringResource(id = R.string.view_all_text),
                                icon = null,
                                onButtonClicked = onViewAllScreenTimeStats,
                                borderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                buttonTextStyle = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Tasks Stats
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.tasks_text))
                }

                // Tasks Chart
                item {
                    TasksStatisticsCard(
                        barChartState = tasksChartState.value,
                        selectedDayText = tasksStatsUiState.dailyTasksState.dayText,
                        selectedDayTasksDone = tasksStatsUiState.dailyTasksState.dailyTasks.completedTasksCount,
                        selectedDayTasksPending = tasksStatsUiState.dailyTasksState.dailyTasks.pendingTasksCount,
                        totalWeekTaskCount = tasksStatsUiState.weeklyTasksState.totalWeekTasksCount,
                        selectedDayIsoNumber = tasksStatsUiState.selectedDay,
                        onBarClicked = { onTasksSelectDay(it) },
                        weekUpdateButton = {}
                    )
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
    }

    // Dialogs
    // App Time Limit Dialog
    if (showAppTimeLimitDialog) {
        when (val limitState = screenTimeUiState.appTimeLimit) {
            is AppTimeLimitState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = { showAppTimeLimitDialog = false },
                    initialAppTimeLimit = limitState.timeLimit,
                    onSaveTimeLimit = onSaveTimeLimit
                )
            }
            else -> {
                CircularProgressDialog(
                    onDismiss = { showAppTimeLimitDialog = false },
                    loadingText = stringResource(id = R.string.loading_text)
                )
            }
        }
    }
}