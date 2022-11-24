package work.racka.reluct.ui.screens.tasks.search

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.SearchData
import work.racka.reluct.common.model.states.tasks.SearchTasksState
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.ScrollToTop
import work.racka.reluct.compose.common.components.cards.taskEntry.EntryType
import work.racka.reluct.compose.common.components.cards.taskEntry.TaskEntry
import work.racka.reluct.compose.common.components.images.ImageWithDescription
import work.racka.reluct.compose.common.components.resources.painterResource
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.textfields.search.MaterialSearchBar
import work.racka.reluct.compose.common.components.util.FetchMoreDataHandler
import work.racka.reluct.compose.common.components.util.rememberScrollContext
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun TasksSearchUI(
    snackbarState: SnackbarHostState,
    uiState: State<SearchTasksState>,
    fetchMoreData: () -> Unit,
    onSearch: (query: String) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val focusRequester = remember {
        FocusRequester()
    }

    val scope = rememberCoroutineScope()

    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            uiState.value.shouldUpdateData && uiState.value.searchData !is SearchData.Loading
        },
        onFetchData = fetchMoreData
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.padding(vertical = Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.Rounded.Close, "Close")
                }
                MaterialSearchBar(
                    value = uiState.value.searchQuery,
                    onSearch = { onSearch(it) },
                    onDismissSearchClicked = { onSearch("") },
                    focusRequester = focusRequester
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Show Empty Graphic
            AnimatedVisibility(
                visible = uiState.value.searchData is SearchData.Empty,
                modifier = Modifier.fillMaxWidth(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    ImageWithDescription(
                        painter = painterResource(SharedRes.assets.file_search),
                        imageSize = 200.dp,
                        description = stringResource(SharedRes.strings.search_not_found_text),
                    )
                }
            }

            // Tasks
            TasksLazyColumn(
                uiStateProvider = { uiState.value },
                onTaskClicked = onTaskClicked,
                onToggleTaskDone = onToggleTaskDone,
                listState = listState
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
private fun TasksLazyColumn(
    uiStateProvider: () -> SearchTasksState,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val uiState = remember { derivedStateOf { uiStateProvider() } }

    AnimatedVisibility(
        visible = uiState.value.searchData !is SearchData.Empty,
        modifier = modifier.fillMaxSize(),
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) { // Show Searched Tasks
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.SmallPadding.size)
        ) {
            items(
                items = uiState.value.searchData.tasksData,
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

            // Loading when fetching more data
            item {
                if (uiState.value.searchData is SearchData.Loading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .padding(Dimens.MediumPadding.size)
                    )
                }
            }

            // Bottom Space for spaceBy
            // Needed so that the load more indicator is shown
            item {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = Dimens.ExtraLargePadding.size)
                )
            }
        }
    }
}
