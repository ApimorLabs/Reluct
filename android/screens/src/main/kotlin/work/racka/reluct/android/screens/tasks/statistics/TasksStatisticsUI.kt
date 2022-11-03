package work.racka.reluct.android.screens.tasks.statistics

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.statistics.BarChartDefaults
import work.racka.reluct.android.compose.components.cards.statistics.ChartData
import work.racka.reluct.android.compose.components.cards.statistics.tasks.TasksStatisticsCard
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.TaskEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.getTasksBarChartBars
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun TasksStatisticsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: TasksStatisticsState,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
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

    // Tasks Stats Chart
    val barColor = BarChartDefaults.barColor
    val tasksChartData by produceState(
        initialValue = ChartData(
            isLoading = uiState.weeklyTasksState is WeeklyTasksState.Loading
        ),
        uiState.weeklyTasksState
    ) {
        value = ChartData(
            data = getTasksBarChartBars(uiState.weeklyTasksState.weeklyTasks, barColor),
            isLoading = uiState.weeklyTasksState is WeeklyTasksState.Loading
        )
    }

    val snackbarModifier = if (scrollContext.isTop) {
        Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
    } else Modifier.navigationBarsPadding()

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }

                // Chart
                item {
                    TasksStatisticsCard(
                        chartData = tasksChartData,
                        selectedDayText = uiState.dailyTasksState.dayText,
                        selectedDayTasksDone = uiState.dailyTasksState.dailyTasks.completedTasksCount,
                        selectedDayTasksPending = uiState.dailyTasksState.dailyTasks.pendingTasksCount,
                        totalWeekTaskCount = uiState.weeklyTasksState.totalWeekTasksCount,
                        selectedDayIsoNumber = uiState.selectedDay,
                        onBarClicked = { onSelectDay(it) },
                        weekUpdateButton = {
                            ValueOffsetButton(
                                text = uiState.selectedWeekText,
                                offsetValue = uiState.weekOffset,
                                onOffsetValueChange = { onUpdateWeekOffset(it) },
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                shape = Shapes.large,
                                incrementEnabled = uiState.weekOffset < 0
                            )
                        }
                    )
                }

                // No Tasks Animation
                if (uiState.dailyTasksState is DailyTasksState.Empty) {
                    item {
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
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
                }

                if (uiState.dailyTasksState.dailyTasks.pendingTasks.isNotEmpty()) {
                    stickyHeader {
                        ListGroupHeadingHeader(text = stringResource(R.string.not_done_tasks_header))
                    }

                    items(
                        items = uiState.dailyTasksState.dailyTasks.pendingTasks,
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
                }

                if (uiState.dailyTasksState.dailyTasks.completedTasks.isNotEmpty()) {
                    stickyHeader {
                        ListGroupHeadingHeader(text = stringResource(R.string.done_tasks_header))
                    }

                    items(
                        items = uiState.dailyTasksState.dailyTasks.completedTasks,
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
                }

                item {
                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = uiState.dailyTasksState is DailyTasksState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
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
    }
}