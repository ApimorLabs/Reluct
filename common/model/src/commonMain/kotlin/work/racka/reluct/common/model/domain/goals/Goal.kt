package work.racka.reluct.common.model.domain.goals

import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.common.model.domain.appInfo.AppInfo

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: ImmutableList<AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalDuration: GoalDuration,
    val goalType: GoalType
)
