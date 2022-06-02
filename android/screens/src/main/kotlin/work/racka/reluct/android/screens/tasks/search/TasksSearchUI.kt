package work.racka.reluct.android.screens.tasks.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.cards.task_entry.EntryType
import work.racka.reluct.android.compose.components.cards.task_entry.TaskEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.textfields.search.MaterialSearchBar
import work.racka.reluct.android.compose.components.topBar.ReluctContentTopBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.model.domain.tasks.Task
import work.racka.reluct.common.model.states.tasks.SearchData
import work.racka.reluct.common.model.states.tasks.SearchTasksState

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun TasksSearchUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: SearchTasksState,
    onSearch: (query: String) -> Unit,
    onTaskClicked: (task: Task) -> Unit,
    onToggleTaskDone: (isDone: Boolean, task: Task) -> Unit,
) {

    val listState = rememberLazyListState()
    val focusRequester = remember {
        FocusRequester()
    }

    val scrollBehavior = remember { TopAppBarDefaults.enterAlwaysScrollBehavior() }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            ReluctContentTopBar(
                minShrinkHeight = 36.dp,
                scrollBehavior = scrollBehavior
            ) {
                MaterialSearchBar(
                    modifier = Modifier
                        .padding(vertical = Dimens.SmallPadding.size)
                        .statusBarsPadding(),
                    value = uiState.searchQuery,
                    onSearch = { onSearch(it) },
                    onDismissSearchClicked = { onSearch("") },
                    focusRequester = focusRequester
                )
            }
        },
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
                            task = item,
                            entryType = EntryType.CompletedTask,
                            onEntryClick = { onTaskClicked(item) },
                            onCheckedChange = { onToggleTaskDone(it, item) }
                        )
                    }

                    // Loading when fetching more data
                    item {
                        if (uiState.searchData is SearchData.Loading) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                LinearProgressIndicator()
                            }
                        }
                    }

                    // Bottom Space for spaceBy
                    // Needed so that the load more indicator is shown
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            }
        }
    }

}