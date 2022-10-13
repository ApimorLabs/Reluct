package work.racka.reluct.android.screens.goals.active

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import work.racka.reluct.common.features.goals.active.states.ActiveGoalsState
import work.racka.reluct.common.features.goals.active.states.GoalsListState
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class, ExperimentalMaterialApi::class
)
@Composable
internal fun ActiveGoalsUI(
    modifier: Modifier = Modifier,
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    scaffoldState: BottomSheetScaffoldState,
    uiState: ActiveGoalsState,
    fetchMoreData: () -> Unit,
    onAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onGoalClicked: (Goal) -> Unit
) {
    val listState = rememberLazyListState()
    val scrollContext = rememberScrollContext(listState = listState)

    val scope = rememberCoroutineScope()

    LaunchedEffect(scrollContext.isBottom) {
        if (scrollContext.isBottom && uiState.goalsListState.shouldUpdateData
            && uiState.goalsListState !is GoalsListState.Loading && !uiState.isSyncing
        ) {
            fetchMoreData()
        }
    }

    // Need to evaluate recomposition overhead when user it at the
    // top of the list
    if (scrollContext.isTop) {
        barsVisibility.bottomBar.show()
        barsVisibility.topBar.show()
    } else {
        barsVisibility.bottomBar.hide()
        barsVisibility.topBar.hide()
    }

    val mainScaffoldBottomPadding by remember(mainScaffoldPadding) {
        derivedStateOf {
            mainScaffoldPadding.calculateBottomPadding()
        }
    }

    val snackbarModifier = if (scrollContext.isTop) Modifier
    else Modifier.navigationBarsPadding()

    BottomSheetScaffold(
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
                    onButtonClicked = {
                        barsVisibility.bottomBar.hide()
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }
                )
            }
        },
        sheetContent = {
            NewGoalSheet(
                onAddGoal = onAddGoal,
                onCloseSheet = {
                    scope.launch { scaffoldState.bottomSheetState.collapse() }
                }
            )
        },
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
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
                visible = uiState.goalsListState is GoalsListState.Loading,
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
            if (uiState.goalsListState.goals.isEmpty()
                && uiState.goalsListState !is GoalsListState.Loading
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = mainScaffoldBottomPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationWithDescription(
                        lottieResId = R.raw.no_data,
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

                    // Syncing Goals Data Loading
                    item {
                        AnimatedVisibility(
                            modifier = Modifier.fillMaxWidth(),
                            visible = uiState.isSyncing,
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
                        items = uiState.goalsListState.goals,
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
                        if (uiState.goalsListState is GoalsListState.Loading &&
                            uiState.goalsListState.goals.isNotEmpty() && !uiState.isSyncing
                        ) {
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

    }
}
