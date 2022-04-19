package work.racka.reluct.android.screens.tasks.pending

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import work.racka.reluct.android.compose.components.buttons.AddButton
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.GroupedTaskEntries
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.tasks.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.TasksState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
internal fun PendingTasksUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: TasksState,
    onTaskClicked: (task: Task) -> Unit,
    onAddTaskClicked: (task: Task?) -> Unit,
    onToggleTaskDone: (isDone: Boolean, taskId: String) -> Unit,
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
                    modifier = Modifier
                        .navigationBarsPadding(),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = Shapes.large
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AddButton(
                buttonText = stringResource(R.string.add_task_button_text),
                onButtonClicked = {
                    onAddTaskClicked(null)
                },
                expanded = buttonExpanded
            )
        }
    ) {
        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = uiState is TasksState.Loading,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading")
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxSize(),
            visible = uiState is TasksState.PendingTasks,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            if (uiState is TasksState.PendingTasks) {
                // Show Empty Graphic
                if (uiState.overdueTasks.isEmpty() && uiState.tasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No Tasks")
                    }
                } else { // Show Pending Tasks
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        state = listState,
                        verticalArrangement = Arrangement
                            .spacedBy(Dimens.MediumPadding.size)
                    ) {
                        item {
                            GroupedTaskEntries(
                                entryType = EntryType.PendingTaskOverdue,
                                groupTitle = stringResource(R.string.overdue_tasks_header),
                                taskList = uiState.overdueTasks,
                                onEntryClicked = { task ->
                                    onTaskClicked(task)
                                },
                                onCheckedChange = { isDone, taskId ->
                                    onToggleTaskDone(isDone, taskId)
                                }
                            )
                        }

                        uiState.tasks.forEach { taskGroup ->
                            item {
                                GroupedTaskEntries(
                                    entryType = EntryType.PendingTaskOverdue,
                                    groupTitle = taskGroup.key,
                                    taskList = taskGroup.value,
                                    onEntryClicked = { task ->
                                        onTaskClicked(task)
                                    },
                                    onCheckedChange = { isDone, taskId ->
                                        onToggleTaskDone(isDone, taskId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}