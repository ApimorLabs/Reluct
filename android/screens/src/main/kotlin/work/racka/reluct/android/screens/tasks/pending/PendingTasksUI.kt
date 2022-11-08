package work.racka.reluct.android.screens.tasks.pending

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.buttons.CollapsingFloatingButton
import work.racka.reluct.android.compose.components.buttons.ScrollToTop
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.taskEntry.EntryType
import work.racka.reluct.android.compose.components.cards.taskEntry.TaskEntry
import work.racka.reluct.android.compose.components.dialogs.FullScreenLoading
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.tasks.components.FullEmptyTasksIndicator
import work.racka.reluct.android.screens.util.BottomBarVisibilityHandler
import work.racka.reluct.android.screens.util.FetchMoreDataHandler
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.PendingTasksState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PendingTasksUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<PendingTasksState>,
    onTaskClicked: (task: Task) -> Unit,
    onAddTaskClicked: (task: Task?) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    fetchMoreData: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    val scope = rememberCoroutineScope()

    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            uiState.value.shouldUpdateData && uiState.value !is PendingTasksState.Loading
        },
        onFetchData = fetchMoreData
    )

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButton = {
            CollapsingFloatingButton(
                scrollContextState = scrollContext,
                mainScaffoldPadding = mainScaffoldPadding,
                text = stringResource(R.string.new_task_button_text),
                icon = Icons.Rounded.Add,
                onClick = { onAddTaskClicked(null) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) {
            FullScreenLoading(
                modifier = Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding()),
                isLoadingProvider = {
                    uiState.value is PendingTasksState.Loading && uiState.value.tasksData.isEmpty() &&
                        uiState.value.overdueTasksData.isEmpty()
                }
            )

            // Show Empty Graphic
            FullEmptyTasksIndicator(
                showAnimationProvider = {
                    uiState.value !is PendingTasksState.Loading &&
                        uiState.value.overdueTasksData.isEmpty() &&
                        uiState.value.tasksData.isEmpty()
                },
                modifier = Modifier.padding(bottom = mainScaffoldPadding.calculateBottomPadding())
            )

            // Tasks
            PendingTasksLazyList(
                uiStateProvider = { uiState.value },
                listState = listState,
                onTaskClicked = onTaskClicked,
                onToggleTaskDone = onToggleTaskDone,
                mainScaffoldPadding = mainScaffoldPadding
            )

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PendingTasksLazyList(
    uiStateProvider: () -> PendingTasksState,
    listState: LazyListState,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    mainScaffoldPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val uiState = remember { derivedStateOf { uiStateProvider() } }
    val isLoading = remember {
        derivedStateOf {
            uiState.value is PendingTasksState.Loading && uiState.value.tasksData.isNotEmpty() &&
                uiState.value.overdueTasksData.isNotEmpty()
        }
    }
    val showData = remember {
        derivedStateOf {
            uiState.value.tasksData.isNotEmpty() || uiState.value.overdueTasksData.isNotEmpty()
        }
    }

    AnimatedVisibility(
        visible = showData.value,
        modifier = modifier.fillMaxSize(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            stickyHeader {
                val isNotEmpty = remember {
                    derivedStateOf { uiState.value.overdueTasksData.isNotEmpty() }
                }
                if (isNotEmpty.value) {
                    ListGroupHeadingHeader(text = stringResource(R.string.overdue_tasks_header))
                }
            }
            items(
                items = uiState.value.overdueTasksData,
                key = { it.id }
            ) { item ->
                TaskEntry(
                    modifier = Modifier.animateItemPlacement(),
                    task = item,
                    entryType = EntryType.TasksWithOverdue,
                    onEntryClick = { onTaskClicked(item) },
                    onCheckedChange = { onToggleTaskDone(item, it) }
                )
            }

            uiState.value.tasksData.forEach { taskGroup ->
                stickyHeader {
                    ListGroupHeadingHeader(text = taskGroup.key)
                }
                items(
                    items = taskGroup.value,
                    key = { it.id }
                ) { item ->
                    TaskEntry(
                        task = item,
                        entryType = EntryType.PendingTask,
                        onEntryClick = { onTaskClicked(item) },
                        onCheckedChange = { onToggleTaskDone(item, it) }
                    )
                }
            }

            // Loading when fetching more data
            item {
                if (isLoading.value) {
                    LinearProgressIndicator()
                }
            }

            // Bottom Space for spaceBy
            // Needed so that the load more indicator is shown
            item {
                Spacer(
                    modifier = Modifier.padding(mainScaffoldPadding)
                )
            }
        }
    }
}
