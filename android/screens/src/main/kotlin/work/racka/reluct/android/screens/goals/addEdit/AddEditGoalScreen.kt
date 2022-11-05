package work.racka.reluct.android.screens.goals.addEdit

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.add_edit_goal.AddEditGoalViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun AddEditGoalScreen(
    goalId: String?,
    defaultGoalIndex: Int?,
    onExit: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel = getCommonViewModel<AddEditGoalViewModel> {
        parametersOf(goalId, defaultGoalIndex)
    }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        context = context,
        eventsState = events,
        snackbarState = snackbarState,
        onExit = onExit
    )

    AddEditGoalUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onSave = viewModel::saveCurrentGoal,
        onCreateNewGoal = viewModel::newGoal,
        onSyncRelatedApps = viewModel::syncRelatedApps,
        onUpdateGoal = viewModel::updateCurrentGoal,
        onModifyApps = viewModel::modifyRelatedApps,
        onGoBack = onExit
    )
}

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<GoalsEvents>,
    snackbarState: SnackbarHostState,
    onExit: () -> Unit
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is GoalsEvents.GoalSavedMessage -> {
                val msg = context.getString(R.string.saved_goal_value, events.goalName)
                launch {
                    snackbarState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is GoalsEvents.Exit -> onExit()
            else -> {}
        }
    }
}
