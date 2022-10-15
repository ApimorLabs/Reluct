package work.racka.reluct.android.screens.goals.active

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
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
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.R
import work.racka.reluct.common.features.goals.active.ActiveGoalsViewModel
import work.racka.reluct.common.features.goals.active.states.GoalsEvents

@Composable
fun ActiveGoalsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAddGoal: (defaultGoalIndex: Int?) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit
) {

    val scaffoldState = rememberScaffoldState()

    val viewModel = getCommonViewModel<ActiveGoalsViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val events by viewModel.event.collectAsState(initial = GoalsEvents.Nothing)

    val context = LocalContext.current

    LaunchedEffect(events) {
        handleEvents(
            context = context,
            events = events,
            scope = this,
            scaffoldState = scaffoldState
        )
    }

    ActiveGoalsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        uiState = uiState,
        fetchMoreData = viewModel::fetchMoreData,
        onAddGoal = onNavigateToAddGoal,
        onGoalClicked = { onNavigateToGoalDetails(it.id) }
    )
}

private fun handleEvents(
    context: Context,
    events: GoalsEvents,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
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
        else -> {}
    }
}