package work.racka.reluct.android.screens.goals.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
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
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalHeadingSwitchCard
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalTypeAndIntervalLabels
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalValuesCard
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun GoalDetailsUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: GoalDetailsState,
    onEditGoal: (goalId: String) -> Unit,
    onDeleteGoal: (goal: Goal) -> Unit,
    onToggleGoalActive: (goalId: String, isActive: Boolean) -> Unit,
    onGoBack: () -> Unit,
    onSyncData: () -> Unit,
    onUpdateCurrentValue: (goalId: String, value: Long) -> Unit
) {
    val listState = rememberLazyListState()

    var openDeleteDialog by remember { mutableStateOf(false) }
    var openUpdateValueDialog by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
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
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {
            // Loading
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is GoalDetailsState.Loading,
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

            // Goal Details
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
                                if (type == GoalType.NumeralGoal) openUpdateValueDialog = true
                                else onSyncData()
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
                        Spacer(
                            modifier = Modifier
                                .height(innerPadding.calculateBottomPadding())
                        )
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

            // Goal Not Found
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = uiState is GoalDetailsState.NotFound,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimationWithDescription(
                        lottieResId = R.raw.no_data,
                        imageSize = 300.dp,
                        description = stringResource(R.string.goal_not_found_text)
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
            .padding(Dimens.MediumPadding.size)
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