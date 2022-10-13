package work.racka.reluct.common.features.goals.add_edit_goal.states

import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.domain.goals.Goal

data class AddEditGoalState(
    val modifyGoalState: ModifyGoalState = ModifyGoalState.Loading,
    val unSelectedAppsState: GoalAppsState = GoalAppsState.Nothing
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
    val unselectedApps: List<AppInfo>
) {
    object Nothing : GoalAppsState(emptyList())

    data class Data(
        private val apps: List<AppInfo>
    ) : GoalAppsState(apps)

    object Loading : GoalAppsState(emptyList())
}
