package work.racka.reluct.android.screens.goals.details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.date.SelectedDaysOfWeekViewer
import work.racka.reluct.android.compose.components.cards.goalEntry.GoalHeadingSwitchCard
import work.racka.reluct.android.compose.components.cards.goalEntry.GoalTypeAndIntervalLabels
import work.racka.reluct.android.compose.components.cards.goalEntry.GoalValuesCard
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.textfields.texts.ListItemTitle
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.goals.components.AppsListCard
import work.racka.reluct.android.screens.goals.components.UpdateValueDialog
import work.racka.reluct.common.features.goals.details.states.GoalDetailsState
import work.racka.reluct.common.model.domain.goals.Goal
import work.racka.reluct.common.model.domain.goals.GoalInterval
import work.racka.reluct.common.model.domain.goals.GoalType

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun GoalDetailsUI(
    snackbarState: SnackbarHostState,
    uiState: GoalDetailsState,
    onEditGoal: (goalId: String) -> Unit,
    onDeleteGoal: (goal: Goal) -> Unit,
    onToggleGoalActive: (goalId: String, isActive: Boolean) -> Unit,
    onGoBack: () -> Unit,
    onSyncData: () -> Unit,
    onUpdateCurrentValue: (goalId: String, value: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    var openDeleteDialog by remember { mutableStateOf(false) }
    var openUpdateValueDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(R.string.goal_details_text),
                navigationIcon = {
                    IconButton(onClick = onGoBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            if (uiState is GoalDetailsState.Data) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    DetailsBottomBar(
                        onEditGoalClicked = { onEditGoal(uiState.goal.id) },
                        onDeleteGoalClicked = { openDeleteDialog = true }
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {
            AnimatedContent(
                targetState = uiState,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { targetState ->
                when (targetState) {
                    is GoalDetailsState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is GoalDetailsState.NotFound -> {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.no_data,
                            imageSize = 300.dp,
                            description = stringResource(R.string.goal_not_found_text)
                        )
                    }
                    is GoalDetailsState.Data -> {}
                }
            }

            if (uiState is GoalDetailsState.Data) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.MediumPadding.size)
                ) {
                    // Title Card
                    item {
                        GoalHeadingSwitchCard(
                            goal = uiState.goal,
                            onToggleActiveState = onToggleGoalActive,
                        )
                    }

                    // Labels
                    item {
                        GoalTypeAndIntervalLabels(
                            modifier = Modifier.fillMaxWidth(),
                            goal = uiState.goal
                        )
                    }

                    // Target and Current Value
                    item {
                        GoalValuesCard(
                            isLoading = uiState.isSyncing,
                            goal = uiState.goal,
                            onUpdateClicked = { type ->
                                if (type == GoalType.NumeralGoal) {
                                    openUpdateValueDialog = true
                                } else {
                                    onSyncData()
                                }
                            }
                        )
                    }

                    // Show Current Apps
                    if (uiState.goal.goalType == GoalType.AppScreenTimeGoal) {
                        item {
                            ListItemTitle(text = stringResource(id = R.string.selected_apps_text))
                        }

                        item {
                            AppsListCard(apps = uiState.goal.relatedApps)
                        }
                    }

                    // Applicable days
                    if (uiState.goal.goalDuration.goalInterval == GoalInterval.Daily) {
                        item {
                            ListItemTitle(text = stringResource(id = R.string.active_days_text))
                        }

                        item {
                            SelectedDaysOfWeekViewer(
                                selectedDays = uiState.goal.goalDuration.selectedDaysOfWeek,
                                onUpdateDaysOfWeek = {}
                            )
                        }
                    }

                    // Bottom Space
                    item {
                        Spacer(modifier = Modifier)
                    }
                }

                // Update Current Value Dialog
                if (openUpdateValueDialog) {
                    UpdateValueDialog(
                        onDismiss = { openUpdateValueDialog = false },
                        headingText = stringResource(id = R.string.current_value_txt),
                        initialValue = uiState.goal.currentValue,
                        onSaveValue = { onUpdateCurrentValue(uiState.goal.id, it) }
                    )
                }
            }
        }
    }

    // Delete Task Dialog
    if (openDeleteDialog) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            title = {
                Text(text = stringResource(R.string.delete_goal_text))
            },
            text = {
                Text(text = stringResource(R.string.delete_goal_message))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        openDeleteDialog = false
                        if (uiState is GoalDetailsState.Data) {
                            onDeleteGoal(uiState.goal)
                        }
                    }
                )
            },
            dismissButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.cancel),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onButtonClicked = { openDeleteDialog = false }
                )
            }
        )
    }
}

@Composable
private fun DetailsBottomBar(
    onEditGoalClicked: () -> Unit,
    onDeleteGoalClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.MediumPadding.size)
            .padding(bottom = Dimens.MediumPadding.size)
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement
            .spacedBy(Dimens.MediumPadding.size)
    ) {
        ReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(R.string.edit_button_text),
            icon = Icons.Rounded.Edit,
            onButtonClicked = onEditGoalClicked,
            buttonColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = Shapes.large
        )

        OutlinedReluctButton(
            modifier = Modifier.weight(1f),
            buttonText = stringResource(R.string.delete_button_text),
            icon = Icons.Rounded.Delete,
            onButtonClicked = onDeleteGoalClicked,
            borderColor = MaterialTheme.colorScheme.primary,
            shape = Shapes.large
        )
    }
}
