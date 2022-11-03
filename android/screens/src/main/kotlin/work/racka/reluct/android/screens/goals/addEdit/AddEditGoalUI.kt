package work.racka.reluct.android.screens.goals.addEdit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottomSheet.addEditGoal.LazyColumnAddEditGoal
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.ManageAppsDialog
import work.racka.reluct.android.screens.util.BackPressHandler
import work.racka.reluct.common.features.goals.add_edit_goal.states.AddEditGoalState
import work.racka.reluct.common.features.goals.add_edit_goal.states.GoalAppsState
import work.racka.reluct.common.features.goals.add_edit_goal.states.ModifyGoalState
import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AddEditGoalUI(
    snackbarState: SnackbarHostState,
    uiState: AddEditGoalState,
    onSave: () -> Unit,
    onCreateNewGoal: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onUpdateGoal: (goal: Goal) -> Unit,
    onModifyApps: (appInfo: AppInfo, isAdd: Boolean) -> Unit,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modifyGoalState = uiState.modifyGoalState

    val (titleText, closeDialogTitle) = getTitles(modifyGoalState)

    val canGoBack by remember(modifyGoalState) {
        derivedStateOf {
            modifyGoalState !is ModifyGoalState.Data
        }
    }
    var openExitDialog by remember { mutableStateOf(false) }
    var openRelatedAppsDialog by remember { mutableStateOf(false) }

    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) {
            onGoBack()
        } else {
            openExitDialog = true
        }
    }

    BackPressHandler { goBackAttempt(canGoBack) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = titleText,
                navigationIcon = {
                    IconButton(onClick = { goBackAttempt(canGoBack) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
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
                targetState = modifyGoalState,
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { targetState ->
                when (targetState) {
                    is ModifyGoalState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is ModifyGoalState.NotFound -> {
                        LottieAnimationWithDescription(
                            lottieResId = R.raw.no_data,
                            imageSize = 300.dp,
                            description = stringResource(R.string.goal_not_found_text)
                        )
                    }
                    is ModifyGoalState.Saved -> {
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement
                                .spacedBy(Dimens.MediumPadding.size)
                        ) {
                            LottieAnimationWithDescription(
                                lottieResId = R.raw.task_saved,
                                imageSize = 300.dp,
                                description = null
                            )
                            ReluctButton(
                                buttonText = stringResource(R.string.new_goal_text),
                                icon = Icons.Rounded.Add,
                                shape = Shapes.large,
                                buttonColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary,
                                onButtonClicked = onCreateNewGoal
                            )
                            OutlinedReluctButton(
                                buttonText = stringResource(R.string.exit_text),
                                icon = Icons.Rounded.ArrowBack,
                                shape = Shapes.large,
                                borderColor = MaterialTheme.colorScheme.primary,
                                onButtonClicked = onGoBack
                            )
                        }
                    }
                    else -> {}
                }
            }

            if (modifyGoalState is ModifyGoalState.Data) {
                // Add and Edit Column
                LazyColumnAddEditGoal(
                    goal = modifyGoalState.goal,
                    onUpdateGoal = onUpdateGoal,
                    onDiscard = { goBackAttempt(canGoBack) },
                    onSave = { onSave() },
                    onShowAppPicker = {
                        openRelatedAppsDialog = true
                        onSyncRelatedApps()
                    }
                )
            }
        }
    }

    // Manage Related Apps
    if (openRelatedAppsDialog) {
        ManageAppsDialog(
            onDismiss = { openRelatedAppsDialog = false },
            isLoading = uiState.appsState is GoalAppsState.Loading,
            topItemsHeading = stringResource(id = R.string.selected_apps_text),
            bottomItemsHeading = stringResource(id = R.string.apps_text),
            topItems = uiState.appsState.selectedApps,
            bottomItems = uiState.appsState.unselectedApps,
            onTopItemClicked = { app -> onModifyApps(app, false) },
            onBottomItemClicked = { app -> onModifyApps(app, true) }
        )
    }

    // Discard Dialog
    if (openExitDialog) {
        AlertDialog(
            onDismissRequest = { openExitDialog = false },
            title = {
                Text(text = closeDialogTitle)
            },
            text = {
                Text(text = stringResource(R.string.discard_task_message))
            },
            confirmButton = {
                ReluctButton(
                    buttonText = stringResource(R.string.ok),
                    icon = null,
                    shape = Shapes.large,
                    buttonColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onButtonClicked = {
                        openExitDialog = false
                        onGoBack()
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
                    onButtonClicked = { openExitDialog = false }
                )
            }
        )
    }
}

@Composable
private fun getTitles(modifyGoalState: ModifyGoalState) = when (modifyGoalState) {
    is ModifyGoalState.Data -> {
        if (modifyGoalState.isEdit) {
            stringResource(R.string.edit_goal_text) to stringResource(R.string.discard_changes_text)
        } else {
            stringResource(R.string.add_goal_text) to stringResource(R.string.discard_goal_text)
        }
    }
    else -> "• • •" to stringResource(R.string.discard_goal_text)
}