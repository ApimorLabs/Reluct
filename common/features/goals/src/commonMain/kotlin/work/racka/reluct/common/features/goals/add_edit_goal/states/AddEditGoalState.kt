package work.racka.reluct.common.features.goals.add_edit_goal.states

import work.racka.reluct.common.model.domain.appInfo.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal

data class AddEditGoalState(
    val modifyGoalState: ModifyGoalState = ModifyGoalState.Loading,
    val appsState: GoalAppsState = GoalAppsState.Nothing
)

sealed class ModifyGoalState {
    data class Data(
        val goal: Goal,
        val isEdit: Boolean
    ) : ModifyGoalState()

    object Saved : ModifyGoalState()

    object Loading : ModifyGoalState()

    object NotFound : ModifyGoalState()
}

sealed class GoalAppsState(
    val selectedApps: List<AppInfo>,
    val unselectedApps: List<AppInfo>
) {
    object Nothing : GoalAppsState(emptyList(), emptyList())

    data class Data(
        private val selected: List<AppInfo>,
        private val unselected: List<AppInfo>
    ) : GoalAppsState(selectedApps = selected, unselectedApps = unselected)

    object Loading : GoalAppsState(emptyList(), emptyList())
}
