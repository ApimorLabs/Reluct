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
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.android.compose.components.cards.headers.TaskGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.tasks.TasksStatisticsCard
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.TaskEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.DailyTasksState
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun TasksStatisticsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    scaffoldState: ScaffoldState,
    uiState: TasksStatisticsState,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (weekOffsetValue: Int) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
) {
    val listState = rememberLazyListState()

    val barChartState = remember(uiState.weeklyTasksState) {
        derivedStateOf {
            val result = when (val weeklyState = uiState.weeklyTasksState) {
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

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
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
                .padding(bottom = mainScaffoldPadding.calculateBottomPadding())
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
                        barChartState = barChartState.value,
                        selectedDayText = uiState.dailyTasksState.dayText,
                        selectedDayTasksDone = uiState.dailyTasksState.dailyTasks.completedTasksCount,
                        selectedDayTasksPending = uiState.dailyTasksState.dailyTasks.pendingTasksCount,
                        totalWeekTaskCount = uiState.weeklyTasksState.totalWeekTasksCount,
                        selectedDayIsoNumber = uiState.selectedDay,
                        onBarClicked = { onSelectDay(it) }
                    ) {
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
                }

                // No Tasks Animation
                if (uiState.dailyTasksState is DailyTasksState.Empty) {
                    item {
                        AnimatedVisibility(
                            visible = uiState.dailyTasksState is DailyTasksState.Empty,
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
                        TaskGroupHeadingHeader(text = stringResource(R.string.not_done_tasks_header))
                    }

                    items(uiState.dailyTasksState.dailyTasks.pendingTasks) { item ->
                        TaskEntry(
                            playScaleAnimation = true,
                            task = item,
                            entryType = EntryType.TasksWithOverdue,
                            onEntryClick = { onTaskClicked(item) },
                            onCheckedChange = { onToggleTaskDone(it, item) }
                        )
                    }
                }

                if (uiState.dailyTasksState.dailyTasks.completedTasks.isNotEmpty()) {
                    stickyHeader {
                        TaskGroupHeadingHeader(text = stringResource(R.string.done_tasks_header))
                    }

                    items(uiState.dailyTasksState.dailyTasks.completedTasks) { item ->
                        TaskEntry(
                            playScaleAnimation = true,
                            task = item,
                            entryType = EntryType.CompletedTask,
                            onEntryClick = { onTaskClicked(item) },
                            onCheckedChange = { onToggleTaskDone(it, item) }
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
                        CircularProgressIndicator()
                    }
                }


                // Bottom Space for spaceBy
                item {
                    Spacer(modifier = Modifier)
                }
            }
        }
    }
}