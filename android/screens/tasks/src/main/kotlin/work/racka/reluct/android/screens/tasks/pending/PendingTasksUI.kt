package work.racka.reluct.android.screens.tasks.pending

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.AddButton
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.GroupedTaskEntries
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.theme.Dimens
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
    updateToolbarOffset: (toolbarOffset: Float) -> Unit,
) {
    val listState = rememberLazyListState()

    val buttonExpanded = listState.firstVisibleItemIndex <= 0

    // CollapsingToolbar Impl
    val toolbarHeight = 120.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                // Returning Zero so we just observe the scroll but don't execute it
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(key1 = toolbarOffsetHeightPx.value) {
        updateToolbarOffset(toolbarOffsetHeightPx.value)
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
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
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

        AnimatedVisibility(
            modifier = Modifier
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
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
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.no_task_animation,
                            imageSize = 200.dp,
                            description = stringResource(R.string.no_tasks_text)
                        )
                    }
                } else { // Show Pending Tasks
                    LazyColumn(
                        modifier = Modifier
                            .nestedScroll(nestedScrollConnection)
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
                                    onCheckedChange = { isDone, taskId ->
                                        onToggleTaskDone(isDone, taskId)
                                    }
                                )
                            }
                        }

                        uiState.tasks.forEach { taskGroup ->
                            item {
                                GroupedTaskEntries(
                                    entryType = EntryType.PendingTask,
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