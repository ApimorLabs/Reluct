package work.racka.reluct.android.screens.goals.details

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
import work.racka.reluct.common.features.goals.details.GoalDetailsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GoalDetailsScreen(
    goalId: String?,
    onExit: () -> Unit,
    onNavigateToEditGoal: (goalId: String) -> Unit
) {
    val viewModel = getCommonViewModel<GoalDetailsViewModel> { parametersOf(goalId) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val snackbarState = remember { SnackbarHostState() }
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

    GoalDetailsUI(
        snackbarState = snackbarState,
        uiState = uiState,
        onEditGoal = onNavigateToEditGoal,
        onDeleteGoal = viewModel::deleteGoal,
        onToggleGoalActive = viewModel::toggleGoalActiveState,
        onGoBack = onExit,
        onSyncData = viewModel::syncData,
        onUpdateCurrentValue = viewModel::updateCurrentValue
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
        is GoalsEvents.ChangedGoalState -> {
            val msg = if (events.isActive) {
                context.getString(R.string.goal_marked_active)
            } else {
                context.getString(R.string.goal_marked_inactive)
            }
            scope.launch {
                snackbarState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
            }
        }
        is GoalsEvents.DeletedGoal -> {
            val msg = context.getString(R.string.deleted_goal_value, events.goalName)
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
