package work.racka.reluct.android.screens.goals.inactive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import work.racka.reluct.android.compose.components.animations.slideInVerticallyFadeReversed
import work.racka.reluct.android.compose.components.animations.slideOutVerticallyFadeReversed
import work.racka.reluct.android.compose.components.buttons.ReluctFloatingActionButton
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalEntry
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.compose.components.util.rememberScrollContext
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.goals.components.NewGoalSheet
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun InactiveGoalsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    scaffoldState: ScaffoldState,
    uiState: GoalsListState,
    fetchMoreData: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onGoalClicked: (Goal) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)
    var showNewGoalDialog by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // React to scroll position
    LaunchedEffect(scrollContext.isBottom) {
        if (scrollContext.isBottom && uiState.shouldUpdateData
            && uiState !is GoalsListState.Loading
        ) {
            fetchMoreData()
        }
    }

    if (scrollContext.isTop) {
        barsVisibility.bottomBar.show()
    } else {
        barsVisibility.bottomBar.hide()
    }

    val mainScaffoldBottomPadding by remember(mainScaffoldPadding) {
        derivedStateOf {
            mainScaffoldPadding.calculateBottomPadding()
        }
    }

    val snackbarModifier = if (scrollContext.isTop) Modifier
    else Modifier.navigationBarsPadding()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = snackbarModifier,
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            AnimatedVisibility(
                visible = scrollContext.isTop,
                enter = slideInVerticallyFadeReversed(),
                exit = slideOutVerticallyFadeReversed()
            ) {
                ReluctFloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = mainScaffoldBottomPadding),
                    buttonText = stringResource(R.string.new_goal_text),
                    contentDescription = stringResource(R.string.add_icon),
                    icon = Icons.Rounded.Add,
                    onButtonClicked = { showNewGoalDialog = true }
                )
            }
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
                visible = uiState is GoalsListState.Loading,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = mainScaffoldBottomPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // Empty Goals Indicator
            if (uiState.goals.isEmpty()
                && uiState !is GoalsListState.Loading
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = mainScaffoldBottomPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationWithDescription(
                        lottieResId = R.raw.no_task_animation,
                        imageSize = 200.dp,
                        description = stringResource(R.string.no_goals_text)
                    )
                }
            } else {
                // Show Active Goals
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.SmallPadding.size)
                ) {
                    // Top Space
                    item { Spacer(modifier = Modifier) }

                    items(
                        items = uiState.goals,
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
                        if (uiState is GoalsListState.Loading && uiState.goals.isNotEmpty()) {
                            LinearProgressIndicator()
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


        if (showNewGoalDialog) {
            Dialog(onDismissRequest = { showNewGoalDialog = false }) {
                NewGoalSheet(
                    modifier = Modifier.height(500.dp),
                    onAddGoal = onAddGoal
                )
            }
        }

    }
}
