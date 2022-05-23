package work.racka.reluct.android.screens.tasks.done

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.ReluctFloatingActionButton
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.GroupedTaskEntries
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.CompletedTasksState

@Composable
internal fun CompletedTasksUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    scaffoldState: ScaffoldState,
    uiState: CompletedTasksState,
    onTaskClicked: (task: Task) -> Unit,
    onAddTaskClicked: (task: Task?) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
    fetchMoreData: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    if (scrollContext.isBottom && uiState.shouldUpdateData
        && uiState !is CompletedTasksState.Loading
    ) {
        fetchMoreData()
    }

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
            ReluctFloatingActionButton(
                modifier = Modifier
                    .padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
                buttonText = stringResource(R.string.new_task_button_text),
                contentDescription = stringResource(R.string.add_icon),
                icon = Icons.Rounded.Add,
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
                visible = uiState is CompletedTasksState.Loading &&
                        uiState.tasksData.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            // Show Empty Graphic
            if (uiState.tasksData.isEmpty()) {
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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.MediumPadding.size)
                ) {
                    // Top Space
                    item {
                        Spacer(modifier = Modifier)
                    }

                    uiState.tasksData.forEach { taskGroup ->
                        item {
                            GroupedTaskEntries(
                                entryType = EntryType.CompletedTask,
                                groupTitle = taskGroup.key,
                                taskList = taskGroup.value,
                                onEntryClicked = { task ->
                                    onTaskClicked(task)
                                },
                                onCheckedChange = { isDone, task ->
                                    onToggleTaskDone(isDone, task)
                                }
                            )
                        }
                    }

                    // Loading when fetching more data
                    item {
                        if (uiState is CompletedTasksState.Loading &&
                            uiState.tasksData.isNotEmpty()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator()
                            }
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
}