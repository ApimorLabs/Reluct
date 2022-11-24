package work.racka.reluct.ui.screens.tasks.completed

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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.CompletedTasksState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.CollapsingFloatingButton
import work.racka.reluct.compose.common.components.buttons.ScrollToTop
import work.racka.reluct.compose.common.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.compose.common.components.cards.taskEntry.EntryType
import work.racka.reluct.compose.common.components.cards.taskEntry.TaskEntry
import work.racka.reluct.compose.common.components.dialogs.FullScreenLoading
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.util.FetchMoreDataHandler
import work.racka.reluct.compose.common.components.util.rememberScrollContext
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.ui.screens.tasks.components.FullEmptyTasksIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CompletedTasksUI(
    snackbarState: SnackbarHostState,
    uiState: State<CompletedTasksState>,
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
            uiState.value.shouldUpdateData && uiState.value !is CompletedTasksState.Loading
        },
        onFetchData = fetchMoreData
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            CollapsingFloatingButton(
                scrollContextState = scrollContext,
                text = stringResource(SharedRes.strings.new_task_button_text),
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
                isLoadingProvider = {
                    uiState.value is CompletedTasksState.Loading &&
                        uiState.value.tasksData.isEmpty()
                }
            )

            // Show Empty Graphic
            FullEmptyTasksIndicator(
                showAnimationProvider = {
                    uiState.value !is CompletedTasksState.Loading &&
                        uiState.value.tasksData.isEmpty()
                }
            )

            // Tasks
            CompletedTasksLazyList(
                uiStateProvider = { uiState.value },
                listState = listState,
                onTaskClicked = onTaskClicked,
                onToggleTaskDone = onToggleTaskDone,
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
private fun CompletedTasksLazyList(
    uiStateProvider: () -> CompletedTasksState,
    listState: LazyListState,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues = PaddingValues(0.dp),
) {
    val uiState = remember { derivedStateOf { uiStateProvider() } }
    val isLoading = remember {
        derivedStateOf {
            uiState.value is CompletedTasksState.Loading && uiState.value.tasksData.isNotEmpty()
        }
    }
    val showData = remember {
        derivedStateOf { uiState.value.tasksData.isNotEmpty() }
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
                        entryType = EntryType.CompletedTask,
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
