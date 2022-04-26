package work.racka.reluct.android.screens.tasks.pending

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.AddButton
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.GroupedTaskEntries
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun PendingTasksUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    scaffoldState: ScaffoldState,
    uiState: TasksState,
    onTaskClicked: (task: Task) -> Unit,
    onAddTaskClicked: (task: Task?) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
) {
    val listState = rememberLazyListState()

    val buttonExpanded = listState.firstVisibleItemIndex <= 0

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
        floatingActionButton = {
            AddButton(
                modifier = Modifier
                    .padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
                buttonText = stringResource(R.string.new_task_button_text),
                onButtonClicked = {
                    onAddTaskClicked(null)
                },
                expanded = buttonExpanded
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = mainScaffoldPadding.calculateBottomPadding())
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is TasksState.Loading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            if (uiState is TasksState.PendingTasks) {
                // Show Empty Graphic
                if (uiState.overdueTasks.isEmpty() && uiState.tasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.no_task_animation,
                            imageSize = 200.dp,
                            description = stringResource(R.string.no_tasks_text)
                        )
                    }
                } else { // Show Pending Tasks
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

                        if (uiState.overdueTasks.isNotEmpty()) {
                            item {
                                GroupedTaskEntries(
                                    entryType = EntryType.PendingTaskOverdue,
                                    groupTitle = stringResource(R.string.overdue_tasks_header),
                                    taskList = uiState.overdueTasks,
                                    onEntryClicked = { task ->
                                        onTaskClicked(task)
                                    },
                                    onCheckedChange = { isDone, task ->
                                        onToggleTaskDone(isDone, task)
                                    }
                                )
                            }
                        }

                        items(uiState.tasks.toList()) { taskGroup ->
                            GroupedTaskEntries(
                                entryType = EntryType.PendingTask,
                                groupTitle = taskGroup.first,
                                taskList = taskGroup.second,
                                onEntryClicked = { task ->
                                    onTaskClicked(task)
                                },
                                onCheckedChange = { isDone, taskId ->
                                    onToggleTaskDone(isDone, taskId)
                                }
                            )
                        }

                        // Bottom Space
                        item {
                            Spacer(modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}