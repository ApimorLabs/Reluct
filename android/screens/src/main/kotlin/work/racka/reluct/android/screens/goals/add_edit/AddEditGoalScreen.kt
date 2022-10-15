package work.racka.reluct.android.screens.goals.add_edit

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.add_edit_goal.AddEditGoalViewModel

@Composable
fun AddEditGoalScreen(
    goalId: String?,
    defaultGoalIndex: Int?,
    onExit: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel = getCommonViewModel<AddEditGoalViewModel> {
        parametersOf(goalId, defaultGoalIndex)
    }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = GoalsEvents.Nothing)

    val context = LocalContext.current
    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            scaffoldState = scaffoldState,
            onExit = onExit
        )
    }

    AddEditGoalUI(
        scaffoldState = scaffoldState,
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
    scaffoldState: ScaffoldState,
    onExit: () -> Unit
) {
    when (events) {
        is GoalsEvents.GoalSavedMessage -> {
            val msg = context.getString(R.string.saved_goal_value, events.goalName)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is GoalsEvents.Exit -> onExit()
        else -> {}
    }
}