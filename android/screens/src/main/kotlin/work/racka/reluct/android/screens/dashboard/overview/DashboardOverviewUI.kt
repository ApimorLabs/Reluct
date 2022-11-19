package work.racka.reluct.android.screens.dashboard.overview

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
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.buttons.ScrollToTop
import work.racka.reluct.android.compose.components.cards.goalEntry.GoalEntry
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.permissions.PermissionsCard
import work.racka.reluct.android.compose.components.cards.statistics.piechart.DailyScreenTimePieChart
import work.racka.reluct.android.compose.components.cards.taskEntry.EntryType
import work.racka.reluct.android.compose.components.cards.taskEntry.TaskEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.screens.dashboard.components.getScreenTimePieChartData
import work.racka.reluct.android.screens.screentime.components.UsagePermissionDialog
import work.racka.reluct.android.screens.util.*
import work.racka.reluct.common.features.dashboard.overview.states.DashboardOverviewState
import work.racka.reluct.common.features.dashboard.overview.states.TodayScreenTimeState
import work.racka.reluct.common.features.dashboard.overview.states.TodayTasksState
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DashboardOverviewUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarHostState: SnackbarHostState,
    uiState: State<DashboardOverviewState>,
    getUsageData: (isGranted: Boolean) -> Unit,
    openScreenTimeStats: () -> Unit,
    openPendingTask: (Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onGoalClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    val chartData = getScreenTimePieChartData(
        appsUsageProvider = { uiState.value.todayScreenTimeState.usageStats.appsUsageList },
        isLoadingProvider = { uiState.value.todayScreenTimeState is TodayScreenTimeState.Loading }
    )

    var usagePermissionGranted by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

    PermissionCheckHandler {
        if (!usagePermissionGranted) {
            usagePermissionGranted = checkUsageAccessPermissions(context)
            getUsageData(usagePermissionGranted)
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

                // Pie Chart
                item {
                    DailyScreenTimePieChart(
                        chartData = chartData,
                        unlockCountProvider = {
                            uiState.value.todayScreenTimeState.usageStats.unlockCount
                        },
                        screenTimeTextProvider = {
                            uiState.value.todayScreenTimeState.usageStats.formattedTotalScreenTime
                        },
                        onClick = openScreenTimeStats
                    )
                }

                // Tasks and Goals
                tasksAndGoals(
                    uiStateProvider = { uiState.value },
                    onOpenPendingTask = openPendingTask,
                    onToggleTaskDone = onToggleTaskDone,
                    onGoalClicked = onGoalClicked
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
                onScrollToTop = {
                    scope.launch { listState.animateScrollToItem(0) }
                }
            )
        }
    }

    // Permission Dialog
    // Go To Usage Access Dialog
    UsagePermissionDialog(openDialog = openDialog, onClose = { openDialog.value = false })
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.tasksAndGoals(
    uiStateProvider: () -> DashboardOverviewState,
    onOpenPendingTask: (Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    onGoalClicked: (Goal) -> Unit,
) {
    // Tasks
    stickyHeader {
        ListGroupHeadingHeader(text = stringResource(R.string.upcoming_tasks_text))
    }

    if (uiStateProvider().todayTasksState is TodayTasksState.Loading) {
        item { LinearProgressIndicator() }
    }

    // No Tasks Animation
    if (uiStateProvider().todayTasksState.pending.isEmpty()) {
        item {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimationWithDescription(
                    lottieResId = R.raw.no_task_animation,
                    imageSize = 200.dp,
                    description = stringResource(R.string.no_tasks_text)
                )
            }
        }
    }

    // Upcoming Tasks
    items(items = uiStateProvider().todayTasksState.pending, key = { it.id }) { item ->
        TaskEntry(
            modifier = Modifier.animateItemPlacement(),
            task = item,
            entryType = EntryType.TasksWithOverdue,
            onEntryClick = { onOpenPendingTask(item) },
            onCheckedChange = { onToggleTaskDone(it, item) }
        )
    }

    // Current Active Goals
    if (uiStateProvider().goals.isNotEmpty()) {
        stickyHeader {
            ListGroupHeadingHeader(text = stringResource(R.string.active_goals_text))
        }

        items(uiStateProvider().goals, key = { it.id }) { goal ->
            GoalEntry(
                modifier = Modifier.animateItemPlacement(),
                goal = goal,
                onEntryClick = { onGoalClicked(goal) }
            )
        }
    }
}
