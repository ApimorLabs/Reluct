package work.racka.reluct.android.screens.tasks.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.buttons.ReluctFloatingActionButton
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.TaskEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.textfields.search.MaterialSearchBar
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.SearchData
import work.racka.reluct.common.model.states.tasks.SearchTasksState

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun TasksSearchUI(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState,
    uiState: SearchTasksState,
    fetchMoreData: () -> Unit,
    onSearch: (query: String) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (task: Task, isDone: Boolean) -> Unit,
) {

    val listState = rememberLazyListState()
    val scrollContext by rememberScrollContext(listState = listState)
    val focusRequester = remember {
        FocusRequester()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(scrollContext.isBottom) {
        if (scrollContext.isBottom && uiState.shouldUpdateData
            && uiState.searchData !is SearchData.Loading
        ) {
            fetchMoreData()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            MaterialSearchBar(
                modifier = Modifier
                    .padding(vertical = Dimens.SmallPadding.size)
                    .statusBarsPadding(),
                value = uiState.searchQuery,
                onSearch = { onSearch(it) },
                onDismissSearchClicked = { onSearch("") },
                focusRequester = focusRequester
            )
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            // Show Empty Graphic
            if (uiState.searchData is SearchData.Empty) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationWithDescription(
                        lottieResId = R.raw.search_animation,
                        imageSize = 200.dp,
                        description = stringResource(R.string.search_not_found_text)
                    )
                }
            } else { // Show Searched Tasks
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.SmallPadding.size)
                ) {

                    items(
                        items = uiState.searchData.tasksData,
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
                        if (uiState.searchData is SearchData.Loading) {
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
                                .navigationBarsPadding()
                        )
                    }
                }
            }

            // Scroll To Top
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = !scrollContext.isTop,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                ReluctFloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = Dimens.MediumPadding.size)
                        .navigationBarsPadding(),
                    buttonText = "",
                    contentDescription = stringResource(R.string.scroll_to_top),
                    icon = Icons.Rounded.ArrowUpward,
                    onButtonClicked = {
                        scope.launch { listState.animateScrollToItem(0) }
                    },
                    expanded = false
                )
            }
        }
    }

}