package work.racka.reluct.android.screens.goals.active

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.buttons.ScrollToTop
import work.racka.reluct.android.compose.components.cards.goalEntry.GoalEntry
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.goals.components.EmptyGoalsIndicator
import work.racka.reluct.android.screens.goals.components.NewGoalFloatingButton
import work.racka.reluct.android.screens.goals.components.NewGoalSheet
import work.racka.reluct.android.screens.util.BottomBarVisibilityHandler
import work.racka.reluct.android.screens.util.FetchMoreDataHandler
import work.racka.reluct.android.screens.util.getSnackbarModifier
import work.racka.reluct.common.features.goals.active.states.ActiveGoalsState
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun ActiveGoalsUI(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    snackbarState: SnackbarHostState,
    uiState: State<ActiveGoalsState>,
    fetchMoreData: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onGoalClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val showNewGoalDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // React to scroll position
    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            val canUpdate = uiState.value.goalsListState.shouldUpdateData
            val isNotLoading =
                uiState.value.goalsListState !is GoalsListState.Loading && !uiState.value.isSyncing
            canUpdate && isNotLoading
        },
        onFetchData = fetchMoreData
    )

    BottomBarVisibilityHandler(
        scrollContext = scrollContext,
        barsVisibility = barsVisibility
    )

    val showEmptyIndicator = remember {
        derivedStateOf {
            uiState.value.goalsListState.goals.isEmpty() &&
                    uiState.value.goalsListState !is GoalsListState.Loading
        }
    }

    val snackbarModifier = getSnackbarModifier(
        mainPadding = mainScaffoldPadding,
        scrollContext = scrollContext
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = snackbarModifier.value,
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
            NewGoalFloatingButton(
                scrollContextState = scrollContext,
                mainScaffoldPadding = mainScaffoldPadding,
                onClick = { showNewGoalDialog.value = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size),
            contentAlignment = Alignment.Center
        ) {
            // Center Loading Indicator
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState.value.goalsListState is GoalsListState.Loading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(mainScaffoldPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Empty Goals Indicator
            EmptyGoalsIndicator(
                showAnimationProvider = { showEmptyIndicator.value },
                modifier = Modifier.padding(mainScaffoldPadding)
            )

            if (!showEmptyIndicator.value) {
                // Show Active Goals
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.SmallPadding.size)
                ) {
                    // Top Space
                    item { Spacer(modifier = Modifier) }

                    // Goals
                    showActiveGoals(
                        uiStateProvider = { uiState.value },
                        onGoalClicked = onGoalClicked
                    )

                    // Bottom Space for spaceBy
                    item {
                        Spacer(
                            modifier = Modifier.padding(mainScaffoldPadding)
                        )
                    }
                }
            }

            // Scroll To Top
            ScrollToTop(
                scrollContext = scrollContext,
                onScrollToTop = { scope.launch { listState.animateScrollToItem(0) } }
            )
        }

        NewGoalDialog(
            openDialog = showNewGoalDialog,
            onClose = { showNewGoalDialog.value = false },
            onAddGoal = onAddGoal
        )
    }
}

@Composable
internal fun NewGoalDialog(
    openDialog: State<Boolean>,
    onClose: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit
) {
    if (openDialog.value) {
        Dialog(onDismissRequest = onClose) {
            NewGoalSheet(
                modifier = Modifier.height(500.dp),
                onAddGoal = onAddGoal
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.showActiveGoals(
    uiStateProvider: () -> ActiveGoalsState,
    onGoalClicked: (Goal) -> Unit
) {
    // Syncing Goals Data Loading
    item {
        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth(),
            visible = uiStateProvider().isSyncing,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator()
            }
        }
    }

    items(
        items = uiStateProvider().goalsListState.goals,
        key = { it.id }
    ) { item ->
        GoalEntry(
            modifier = Modifier.animateItemPlacement(),
            goal = item,
            onEntryClick = { onGoalClicked(item) }
        )
    }

    // Loading when fetching more data
    item {
        if (uiStateProvider().goalsListState is GoalsListState.Loading &&
            uiStateProvider().goalsListState.goals.isNotEmpty() && !uiStateProvider().isSyncing
        ) {
            LinearProgressIndicator()
        }
    }
}
