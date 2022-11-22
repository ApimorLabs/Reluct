package work.racka.reluct.android.screens.goals.active

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.goals.active.ActiveGoalsViewModel
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.compose.common.components.util.BarsVisibility

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ActiveGoalsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel = getCommonViewModel<ActiveGoalsViewModel>()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val events = viewModel.event.collectAsStateWithLifecycle(initialValue = GoalsEvents.Nothing)

    val context = LocalContext.current
    HandleEvents(
        context = context,
        eventsState = events,
        snackbarState = snackbarState
    )

    ActiveGoalsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
        uiState = uiState,
        fetchMoreData = viewModel::fetchMoreData,
        onAddGoal = onNavigateToAddGoal,
        onGoalClicked = { onNavigateToGoalDetails(it.id) }
    )
}

@Composable
private fun HandleEvents(
    context: Context,
    eventsState: State<GoalsEvents>,
    snackbarState: SnackbarHostState,
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
            else -> {}
        }
    }
}
