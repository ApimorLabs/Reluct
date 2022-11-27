package work.racka.reluct.ui.screens.goals.inactive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.buttons.CollapsingFloatingButton
import work.racka.reluct.compose.common.components.buttons.ScrollToTop
import work.racka.reluct.compose.common.components.cards.goalEntry.GoalEntry
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.components.util.FetchMoreDataHandler
import work.racka.reluct.compose.common.components.util.rememberScrollContext
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.ui.screens.goals.active.NewGoalDialog
import work.racka.reluct.ui.screens.goals.components.EmptyGoalsIndicator

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
internal fun InactiveGoalsUI(
    snackbarState: SnackbarHostState,
    uiState: State<GoalsListState>,
    fetchMoreData: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onGoalClicked: (Goal) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    val scope = rememberCoroutineScope()
    val showNewGoalDialog = remember { mutableStateOf(false) }

    // React to scroll position
    FetchMoreDataHandler(
        scrollContext = scrollContext,
        isFetchAllowedProvider = {
            uiState.value.shouldUpdateData && uiState.value !is GoalsListState.Loading
        },
        onFetchData = fetchMoreData
    )

    val showEmptyIndicator = remember {
        derivedStateOf { uiState.value.goals.isEmpty() && uiState.value !is GoalsListState.Loading }
    }

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
                text = stringResource(SharedRes.strings.new_goal_text),
                icon = Icons.Rounded.Add,
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
                visible = uiState.value is GoalsListState.Loading,
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

            // Empty Goals Indicator
            EmptyGoalsIndicator(showAnimationProvider = { showEmptyIndicator.value })

            if (!showEmptyIndicator.value) {
                // Show Active Goals
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size)
                ) {
                    // Top Space
                    item { Spacer(modifier = Modifier) }

                    // In Active Goals
                    showInActiveGoals(
                        uiStateProvider = { uiState.value },
                        onGoalClicked = onGoalClicked
                    )

                    // Bottom Space for spaceBy
                    // Needed so that the load more indicator is shown
                    item {
                        Spacer(
                            modifier = Modifier.height(Dimens.ExtraLargePadding.size)
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

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.showInActiveGoals(
    uiStateProvider: () -> GoalsListState,
    onGoalClicked: (Goal) -> Unit
) {
    items(
        items = uiStateProvider().goals,
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
        if (uiStateProvider() is GoalsListState.Loading && uiStateProvider().goals.isNotEmpty()) {
            LinearProgressIndicator()
        }
    }
}
