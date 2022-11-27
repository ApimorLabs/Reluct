package work.racka.reluct.ui.navigationComponents.tabs

import work.racka.reluct.common.core.navigation.destination.graphs.GoalsConfig
import work.racka.reluct.common.core.navigation.destination.graphs.TasksConfig
import work.racka.reluct.compose.common.components.SharedRes

internal val goalsTabs = arrayOf(
    Pair(GoalsConfig.Active, SharedRes.strings.goals_active),
    Pair(GoalsConfig.Inactive, SharedRes.strings.goals_inactive),
)

internal val tasksTabs = arrayOf(
    Pair(TasksConfig.Pending, SharedRes.strings.tasks_pending),
    Pair(TasksConfig.Completed, SharedRes.strings.tasks_done),
    Pair(TasksConfig.Statistics, SharedRes.strings.tasks_stats),
)
