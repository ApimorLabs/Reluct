package work.racka.reluct.android.screens.goals.add_edit

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottom_sheet.add_edit_goal.LazyColumnAddEditGoal
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
import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AddEditGoalUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: AddEditGoalState,
    onSave: () -> Unit,
    onCreateNewGoal: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onUpdateGoal: (goal: Goal) -> Unit,
    onModifyApps: (appInfo: AppInfo, isAdd: Boolean) -> Unit,
    onGoBack: () -> Unit
) {

    val modifyGoalState = uiState.modifyGoalState

    val titleText = when (modifyGoalState) {
        is ModifyGoalState.Data -> {
            if (modifyGoalState.isEdit) stringResource(R.string.edit_goal_text)
            else stringResource(R.string.add_goal_text)
        }
        else -> "• • •"
    }

    val canGoBack by remember(modifyGoalState) {
        derivedStateOf {
            modifyGoalState !is ModifyGoalState.Data
        }
    }
    var openExitDialog by remember { mutableStateOf(false) }
    var openRelatedAppsDialog by remember { mutableStateOf(false) }

    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onGoBack()
        else openExitDialog = true
    }

    BackPressHandler { goBackAttempt(canGoBack) }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colorScheme.background,
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
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize()
        ) {

            // Loading
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = modifyGoalState is ModifyGoalState.Loading,
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

            // Add or Edit Goal
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = modifyGoalState is ModifyGoalState.Data,
                enter = fadeIn(),
                exit = scaleOut()
            ) {
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

                    // Manage Related Apps
                    if (openRelatedAppsDialog) {
                        ManageAppsDialog(
                            onDismiss = { openRelatedAppsDialog = false },
                            isLoading = uiState.unSelectedAppsState is GoalAppsState.Loading,
                            topItemsHeading = stringResource(id = R.string.selected_apps_text),
                            bottomItemsHeading = stringResource(id = R.string.apps_text),
                            topItems = modifyGoalState.goal.relatedApps,
                            bottomItems = uiState.unSelectedAppsState.unselectedApps,
                            onTopItemClicked = { app -> onModifyApps(app, false) },
                            onBottomItemClicked = { app -> onModifyApps(app, true) }
                        )
                    }
                }
            }

            // Goal Saved
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = modifyGoalState is ModifyGoalState.Saved,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
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
                        buttonText = stringResource(R.string.go_back_button_text),
                        icon = Icons.Rounded.ArrowBack,
                        shape = Shapes.large,
                        borderColor = MaterialTheme.colorScheme.primary,
                        onButtonClicked = onGoBack
                    )
                }
            }

            // Goal Not Found
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxSize(),
                visible = modifyGoalState is ModifyGoalState.NotFound,
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

    // Discard Dialog
    if (openExitDialog) {
        AlertDialog(
            onDismissRequest = { openExitDialog = false },
            title = {
                Text(text = stringResource(R.string.discard_goal_text))
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