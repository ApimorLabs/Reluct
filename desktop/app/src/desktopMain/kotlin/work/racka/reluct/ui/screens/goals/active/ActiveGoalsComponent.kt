package work.racka.reluct.ui.screens.goals.active

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.decompose.commonViewModel
import work.racka.reluct.common.features.goals.active.ActiveGoalsViewModel
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.ui.screens.ComposeRenderer

class ActiveGoalsComponent(
    componentContext: ComponentContext,
    private val onShowDetails: (goalId: String) -> Unit,
    private val onAddGoal: (defaultGoalIndex: Int?) -> Unit,
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<ActiveGoalsViewModel>()

    @Composable
    override fun Render(modifier: Modifier) {
        val snackbarState = remember { SnackbarHostState() }
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.event.collectAsState(initial = GoalsEvents.Nothing)

        HandleEvents(eventsState = events, snackbarState = snackbarState)

        ActiveGoalsUI(
            modifier = modifier,
            snackbarState = snackbarState,
            uiState = uiState,
            fetchMoreData = viewModel::fetchMoreData,
            onAddGoal = onAddGoal,
            onGoalClicked = { onShowDetails(it.id) }
        )
    }

    @Composable
    private fun HandleEvents(
        eventsState: State<GoalsEvents>,
        snackbarState: SnackbarHostState,
    ) {
        LaunchedEffect(eventsState.value) {
            when (val events = eventsState.value) {
                is GoalsEvents.ChangedGoalState -> {
                    val msg = if (events.isActive) {
                        SharedRes.strings.goal_marked_active.localized()
                    } else {
                        SharedRes.strings.goal_marked_inactive.localized()
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
}
