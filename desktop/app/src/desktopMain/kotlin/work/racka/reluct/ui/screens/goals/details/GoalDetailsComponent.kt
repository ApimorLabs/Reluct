package work.racka.reluct.ui.screens.goals.details

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import work.racka.common.mvvm.koin.decompose.commonViewModel
import work.racka.reluct.common.features.goals.active.states.GoalsEvents
import work.racka.reluct.common.features.goals.details.GoalDetailsViewModel
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.ui.screens.ComposeRenderer

class GoalDetailsComponent(
    componentContext: ComponentContext,
    private val goalId: String?,
    private val onEdit: (goalId: String) -> Unit,
    private val onExit: () -> Unit,
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<GoalDetailsViewModel> { parametersOf(goalId) }

    @Composable
    override fun Render(modifier: Modifier) {
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.events.collectAsState(initial = GoalsEvents.Nothing)

        val snackbarState = remember { SnackbarHostState() }
        HandleEvents(
            eventsState = events,
            snackbarState = snackbarState,
            onExit = onExit
        )

        GoalDetailsUI(
            modifier = modifier,
            snackbarState = snackbarState,
            uiState = uiState,
            onEditGoal = onEdit,
            onDeleteGoal = viewModel::deleteGoal,
            onToggleGoalActive = viewModel::toggleGoalActiveState,
            onGoBack = onExit,
            onSyncData = viewModel::syncData,
            onUpdateCurrentValue = viewModel::updateCurrentValue
        )
    }

    @Composable
    private fun HandleEvents(
        eventsState: State<GoalsEvents>,
        snackbarState: SnackbarHostState,
        onExit: () -> Unit
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
                is GoalsEvents.DeletedGoal -> {
                    val msg = StringDesc.ResourceFormatted(
                        SharedRes.strings.deleted_goal_value,
                        events.goalName
                    ).localized()
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
}
