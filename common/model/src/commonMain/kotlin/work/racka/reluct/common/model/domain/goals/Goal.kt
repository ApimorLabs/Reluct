package work.racka.reluct.common.model.domain.goals

import work.racka.reluct.common.model.domain.app_info.AppInfo
import work.racka.reluct.common.model.util.time.Week

data class Goal(
    val id: String,
    val name: String,
    val description: String,
    val isActive: Boolean,
    val relatedApps: List<AppInfo>,
    val targetValue: Long,
    val currentValue: Long,
    val goalInterval: GoalInterval,
    val timeInterval: LongRange?,
    val daysOfWeekSelected: List<Week> = emptyList(),
    val goalType: GoalType
)
