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
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.date.SelectedDaysOfWeekViewer
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalHeadingSwitchCard
import work.racka.reluct.android.compose.components.cards.goal_entry.GoalTypeAndIntervalLabels
import work.racka.reluct.android.compose.components.textfields.texts.ListItemTitle
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.goals.components.AppsListCard
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun GoalDetailsUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: Any,
    onEditGoal: (goalId: String) -> Unit,
    onDeleteGoal: (goalId: String) -> Unit,
    onToggleGoalActive: (goalId: String, isActive: Boolean) -> Unit,
    onGoBack: () -> Unit
) {
    val listState = rememberLazyListState()

    var openDeleteDialog by remember { mutableStateOf(false) }

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
            val goal: Goal? = null // TODO: Put state here
            goal?.let {
                DetailsBottomBar(
                    onEditGoalClicked = { onEditGoal(it.id) },
                    onDeleteGoalClicked = { openDeleteDialog = true }
                )
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
                visible = true,
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
            if (true) {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement
                        .spacedBy(Dimens.MediumPadding.size)
                ) {
                    // Title Card
                    item {
                        GoalHeadingSwitchCard(
                            goal =,
                            onToggleActiveState = onToggleGoalActive
                        )
                    }

                    // Labels
                    item {
                        GoalTypeAndIntervalLabels(
                            modifier = Modifier.fillMaxWidth(),
                            goal =
                        )
                    }

                    // Description
                    item {
                        Text(
                            text = "${stringResource(R.string.description_hint)}: ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current
                                .copy(alpha = .8f)
                        )
                    }

                    // Show Current Apps
                    if (true) {
                        item {
                            ListItemTitle(text = stringResource(id = R.string.selected_apps_text))
                        }

                        item {
                            AppsListCard(apps =)
                        }
                    }

                    // Applicable days
                    if (true) {
                        item {
                            ListItemTitle(text = stringResource(id = R.string.active_days_text))
                        }

                        item {
                            SelectedDaysOfWeekViewer(
                                selectedDays =,
                                onUpdateDaysOfWeek = {}
                            )
                        }
                    }
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
                        val goal: Goal? = null // TODO: Put state here
                        goal?.let { onDeleteGoal(it.id) }
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