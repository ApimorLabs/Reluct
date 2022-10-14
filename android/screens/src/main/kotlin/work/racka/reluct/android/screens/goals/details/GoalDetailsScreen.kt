package work.racka.reluct.android.screens.goals.details

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
import work.racka.reluct.common.features.goals.details.GoalDetailsViewModel

@Composable
fun GoalDetailsScreen(
    goalId: String?,
    onExit: () -> Unit,
    onNavigateToEditGoal: (goalId: String) -> Unit
) {

    val viewModel = getCommonViewModel<GoalDetailsViewModel> { parametersOf(goalId) }
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.events.collectAsState(initial = GoalsEvents.Nothing)

    val scaffoldState = rememberScaffoldState()
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

    GoalDetailsUI(
        scaffoldState = scaffoldState,
        uiState = uiState,
        onEditGoal = onNavigateToEditGoal,
        onDeleteGoal = viewModel::deleteGoal,
        onToggleGoalActive = viewModel::toggleGoalActiveState,
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
        is GoalsEvents.ChangedGoalState -> {
            val msg = if (events.isActive) context.getString(R.string.goal_marked_active)
            else context.getString(R.string.goal_marked_inactive)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is GoalsEvents.DeletedGoal -> {
            val msg = context.getString(R.string.deleted_goal_value, events.goalName)
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