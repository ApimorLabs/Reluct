package work.racka.reluct.android.navigation.toptabs.tasks

import work.racka.reluct.common.core.navigation.composeDestinations.tasks.CompletedTasksDestination
import work.racka.reluct.common.core.navigation.composeDestinations.tasks.PendingTasksDestination
import work.racka.reluct.common.core.navigation.composeDestinations.tasks.TasksStatisticsDestination

enum class TasksTabDestination(val route: String) {
    Tasks(route = PendingTasksDestination.route),
    Done(route = CompletedTasksDestination.route),
    Statistics(route = TasksStatisticsDestination.route);
}
