package work.racka.reluct.common.features.goals.add_edit_goal.states

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
    val selectedApps: ImmutableList<AppInfo>,
    val unselectedApps: ImmutableList<AppInfo>
) {
    object Nothing : GoalAppsState(persistentListOf(), persistentListOf())

    data class Data(
        private val selected: ImmutableList<AppInfo>,
        private val unselected: ImmutableList<AppInfo>
    ) : GoalAppsState(selectedApps = selected, unselectedApps = unselected)

    object Loading : GoalAppsState(persistentListOf(), persistentListOf())
}
