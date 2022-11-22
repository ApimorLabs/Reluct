package work.racka.reluct.android.screens.tasks.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.getWeeklyTasksBarChartData
import work.racka.reluct.android.screens.util.BottomBarVisibilityHandler
import work.racka.reluct.android.screens.util.getSnackbarModifier
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.ValueOffsetButton
import work.racka.reluct.compose.common.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.compose.common.components.cards.statistics.BarChartDefaults
import work.racka.reluct.compose.common.components.cards.statistics.tasks.TasksStatisticsCard
import work.racka.reluct.compose.common.components.cards.taskEntry.EntryType
import work.racka.reluct.compose.common.components.cards.taskEntry.TaskEntry
import work.racka.reluct.compose.common.components.images.LottieAnimationWithDescription
import work.racka.reluct.compose.common.components.util.BarsVisibility
import work.racka.reluct.compose.common.components.util.rememberScrollContext
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TasksStatisticsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<TasksStatisticsState>,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    // Tasks Stats Chart
    val barColor = BarChartDefaults.barColor
    val tasksChartData = getWeeklyTasksBarChartData(
        weeklyTasksProvider = { uiState.value.weeklyTasksState.weeklyTasks },
        isLoadingProvider = { uiState.value.weeklyTasksState is WeeklyTasksState.Loading },
        barColor = barColor
    )

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
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

                // Chart
                item {
                    TasksStatisticsCard(
                        chartData = tasksChartData,
                        selectedDayText = { uiState.value.dailyTasksState.dayText },
                        selectedDayTasksDone = { uiState.value.dailyTasksState.dailyTasks.completedTasksCount },
                        selectedDayTasksPending = { uiState.value.dailyTasksState.dailyTasks.pendingTasksCount },
                        totalWeekTaskCount = { uiState.value.weeklyTasksState.totalWeekTasksCount },
                        selectedDayIsoNumber = { uiState.value.selectedDay },
                        onBarClicked = { onSelectDay(it) },
                        weekUpdateButton = {
                            WeekOffsetButton(
                                textProvider = { uiState.value.selectedWeekText },
                                offsetValueProvider = { uiState.value.weekOffset },
                                isIncrementEnabled = { uiState.value.weekOffset < 0 },
                                onUpdateWeekOffset = onUpdateWeekOffset
                            )
                        }
                    )
                }

                // Tasks
                tasksList(
                    dailyTasksStateProvider = { uiState.value.dailyTasksState },
                    isLoadingProvider = { uiState.value.dailyTasksState is DailyTasksState.Loading },
                    onTaskClicked = onTaskClicked,
                    onToggleTaskDone = onToggleTaskDone
                )

                // Bottom Space for spaceBy
                item {
                    Spacer(
                        modifier = Modifier.padding(mainScaffoldPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.tasksList(
    dailyTasksStateProvider: () -> DailyTasksState,
    isLoadingProvider: () -> Boolean,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit
) {
    // No Tasks Animation
    item {
        val isEmpty = remember {
            derivedStateOf { dailyTasksStateProvider() is DailyTasksState.Empty }
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
                    lottieResource = SharedRes.files.no_task_animation,
                    imageSize = 200.dp,
                    description = stringResource(R.string.no_tasks_text),
                    descriptionTextStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    stickyHeader {
        val isNotEmpty = remember {
            derivedStateOf { dailyTasksStateProvider().dailyTasks.pendingTasks.isNotEmpty() }
        }
        if (isNotEmpty.value) {
            ListGroupHeadingHeader(text = stringResource(R.string.not_done_tasks_header))
        }
    }

    items(
        items = dailyTasksStateProvider().dailyTasks.pendingTasks,
        key = { it.id }
    ) { item ->
        TaskEntry(
            modifier = Modifier.animateItemPlacement(),
            task = item,
            entryType = EntryType.TasksWithOverdue,
            onEntryClick = { onTaskClicked(item) },
            onCheckedChange = { onToggleTaskDone(item, it) },
            playAnimation = true
        )
    }

    stickyHeader {
        val isNotEmpty = remember {
            derivedStateOf { dailyTasksStateProvider().dailyTasks.completedTasks.isNotEmpty() }
        }
        if (isNotEmpty.value) {
            ListGroupHeadingHeader(text = stringResource(R.string.done_tasks_header))
        }
    }

    items(
        items = dailyTasksStateProvider().dailyTasks.completedTasks,
        key = { it.id }
    ) { item ->
        TaskEntry(
            modifier = Modifier.animateItemPlacement(),
            task = item,
            entryType = EntryType.CompletedTask,
            onEntryClick = { onTaskClicked(item) },
            onCheckedChange = { onToggleTaskDone(item, it) }
        )
    }

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
}

@Composable
private fun WeekOffsetButton(
    textProvider: () -> String,
    offsetValueProvider: () -> Int,
    isIncrementEnabled: () -> Boolean,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = remember { derivedStateOf { textProvider() } }
    val offsetValue = remember { derivedStateOf { offsetValueProvider() } }
    val incrementEnabled = remember { derivedStateOf { isIncrementEnabled() } }

    ValueOffsetButton(
        modifier = modifier,
        text = text.value,
        offsetValue = offsetValue.value,
        onOffsetValueChange = onUpdateWeekOffset,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = Shapes.large,
        incrementEnabled = incrementEnabled.value
    )
}
