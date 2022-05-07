package work.racka.reluct.android.screens.tasks.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.tasks.TasksStatisticsCard
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksStatisticsState
import work.racka.reluct.common.model.states.tasks.WeeklyTasksState

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
    ) {
        Box(
            modifier = Modifier
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
                    .spacedBy(Dimens.MediumPadding.size)
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

                // Bottom Space
                item {
                    Spacer(modifier = Modifier)
                }
            }
        }
    }
}