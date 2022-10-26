package work.racka.reluct.android.screens.goals.add_edit

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            snackbarState = snackbarState,
            onExit = onExit
        )
    }

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

private fun handleEvents(
    context: Context,
    events: GoalsEvents,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    onExit: () -> Unit
) {
    when (events) {
        is GoalsEvents.GoalSavedMessage -> {
            val msg = context.getString(R.string.saved_goal_value, events.goalName)
            scope.launch {
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