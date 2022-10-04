package work.racka.reluct.common.model.domain.goals

import work.racka.reluct.common.model.util.time.Week

data class GoalDuration(
    val goalInterval: GoalInterval,
    val timeRangeInMillis: LongRange?,
    val selectedDaysOfWeek: List<Week>
)
