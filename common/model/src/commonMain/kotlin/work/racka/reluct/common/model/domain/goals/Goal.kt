package work.racka.reluct.common.model.domain.goals

import work.racka.reluct.common.model.domain.app_info.AppInfo

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: List<AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalDuration: GoalDuration,
    val goalType: GoalType
)
