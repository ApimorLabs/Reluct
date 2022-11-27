package work.racka.reluct.ui.screens.goals.addEdit

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
import work.racka.reluct.common.features.goals.addEditGoal.AddEditGoalViewModel
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.ui.screens.ComposeRenderer

class AddEditGoalComponent(
    componentContext: ComponentContext,
    private val goalId: String?,
    private val defaultGoalIndex: Int?,
    private val onExit: () -> Unit
) : ComponentContext by componentContext, ComposeRenderer {

    private val viewModel by commonViewModel<AddEditGoalViewModel> {
        parametersOf(goalId, defaultGoalIndex)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val snackbarState = remember { SnackbarHostState() }
        val uiState = viewModel.uiState.collectAsState()
        val events = viewModel.events.collectAsState(initial = GoalsEvents.Nothing)

        HandleEvents(
            eventsState = events,
            snackbarState = snackbarState,
            onExit = onExit
        )

        AddEditGoalUI(
            modifier = modifier,
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
        eventsState: State<GoalsEvents>,
        snackbarState: SnackbarHostState,
        onExit: () -> Unit
    ) {
        LaunchedEffect(eventsState.value) {
            when (val events = eventsState.value) {
                is GoalsEvents.GoalSavedMessage -> {
                    val msg = StringDesc.ResourceFormatted(
                        SharedRes.strings.saved_goal_value,
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
