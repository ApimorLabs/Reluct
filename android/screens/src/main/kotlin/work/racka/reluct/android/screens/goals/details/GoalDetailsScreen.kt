package work.racka.reluct.android.screens.goals.details

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
import work.racka.reluct.common.features.goals.details.GoalDetailsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun GoalDetailsScreen(
    goalId: String?,
    onExit: () -> Unit,
    onNavigateToEditGoal: (goalId: String) -> Unit
) {
    val viewModel = getCommonViewModel<GoalDetailsViewModel> { parametersOf(goalId) }
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.events.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val snackbarState = remember { SnackbarHostState() }
    val context = LocalContext.current
    HandleEvents(
        context = context,
        eventsState = events,
        snackbarState = snackbarState,
        onExit = onExit
    )

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

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<GoalsEvents>,
    snackbarState: SnackbarHostState,
    onExit: () -> Unit
) {
    LaunchedEffect(eventsState.value) {
        when (val events = eventsState.value) {
            is GoalsEvents.ChangedGoalState -> {
                val msg = if (events.isActive) {
                    context.getString(R.string.goal_marked_active)
                } else {
                    context.getString(R.string.goal_marked_inactive)
                }
                launch {
                    snackbarState.showSnackbar(
                        message = msg,
                        duration = SnackbarDuration.Short
                    )
                }
            }
            is GoalsEvents.DeletedGoal -> {
                val msg = context.getString(R.string.deleted_goal_value, events.goalName)
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
