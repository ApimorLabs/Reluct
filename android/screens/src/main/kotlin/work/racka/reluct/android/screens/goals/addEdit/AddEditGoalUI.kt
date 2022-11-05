package work.racka.reluct.android.screens.goals.addEdit

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.bottomSheet.addEditGoal.LazyColumnAddEditGoal
import work.racka.reluct.android.compose.components.buttons.OutlinedReluctButton
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.dialogs.DiscardPromptDialog
import work.racka.reluct.android.compose.components.images.LottieAnimationWithDescription
import work.racka.reluct.android.compose.components.topBar.ReluctSmallTopAppBar
import work.racka.reluct.android.compose.components.util.EditTitles
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
    uiState: State<AddEditGoalState>,
    onSave: () -> Unit,
    onCreateNewGoal: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onUpdateGoal: (goal: Goal) -> Unit,
    onModifyApps: (appInfo: AppInfo, isAdd: Boolean) -> Unit,
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val modifyGoalState = remember { derivedStateOf { uiState.value.modifyGoalState } }

    val context = LocalContext.current
    val titles = getTitles(
        modifyGoalStateProvider = { modifyGoalState.value },
        context = context
    )

    val canGoBack by remember {
        derivedStateOf {
            modifyGoalState.value !is ModifyGoalState.Data
        }
    }
    val openExitDialog = remember { mutableStateOf(false) }
    val openRelatedAppsDialog = remember { mutableStateOf(false) }
    // Call this when you trying to Go Back safely!
    fun goBackAttempt(canGoBack: Boolean) {
        if (canGoBack) onGoBack() else openExitDialog.value = true
    }
    BackPressHandler { goBackAttempt(canGoBack) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            ReluctSmallTopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = titles.value.appBarTitle,
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
                targetState = modifyGoalState.value,
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

            EditGoalList(
                getModifyGoalState = { modifyGoalState.value },
                onUpdateGoal = onUpdateGoal,
                onGoBack = { goBackAttempt(canGoBack) },
                onSyncRelatedApps = onSyncRelatedApps,
                onOpenAppsDialog = { openRelatedAppsDialog.value = true },
                onSave = onSave
            )
        }
    }

    // Manage Related Apps
    ManageAppsDialog(
        openDialog = openRelatedAppsDialog,
        onDismiss = { openRelatedAppsDialog.value = false },
        isLoadingProvider = { uiState.value.appsState is GoalAppsState.Loading },
        topItemsHeading = stringResource(id = R.string.selected_apps_text),
        bottomItemsHeading = stringResource(id = R.string.apps_text),
        topItems = { uiState.value.appsState.selectedApps },
        bottomItems = { uiState.value.appsState.unselectedApps },
        onTopItemClicked = { app -> onModifyApps(app, false) },
        onBottomItemClicked = { app -> onModifyApps(app, true) }
    )

    // Discard Dialog
    DiscardPromptDialog(
        dialogTitleProvider = { titles.value.dialogTitle },
        openDialog = openExitDialog,
        onClose = { openExitDialog.value = false },
        onGoBack = onGoBack
    )
}

@Composable
private fun EditGoalList(
    getModifyGoalState: () -> ModifyGoalState,
    onUpdateGoal: (goal: Goal) -> Unit,
    onGoBack: () -> Unit,
    onSyncRelatedApps: () -> Unit,
    onOpenAppsDialog: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val goalState = getModifyGoalState()

    if (goalState is ModifyGoalState.Data) {
        LazyColumnAddEditGoal(
            modifier = modifier,
            goal = goalState.goal,
            onUpdateGoal = onUpdateGoal,
            onDiscard = onGoBack,
            onSave = { onSave() },
            onShowAppPicker = {
                onOpenAppsDialog()
                onSyncRelatedApps()
            }
        )
    }
}

@Composable
private fun getTitles(modifyGoalStateProvider: () -> ModifyGoalState, context: Context) =
    remember(context) {
        derivedStateOf {
            when (val goalState = modifyGoalStateProvider()) {
                is ModifyGoalState.Data -> {
                    if (goalState.isEdit) {
                        EditTitles(
                            appBarTitle = context.getString(R.string.edit_goal_text),
                            dialogTitle = context.getString(R.string.discard_changes_text)
                        )
                    } else {
                        EditTitles(
                            appBarTitle = context.getString(R.string.add_goal_text),
                            dialogTitle = context.getString(R.string.discard_goal_text)
                        )
                    }
                }
                else -> EditTitles(
                    appBarTitle = "• • •",
                    dialogTitle = context.getString(R.string.discard_goal_text)
                )
            }
        }
    }
