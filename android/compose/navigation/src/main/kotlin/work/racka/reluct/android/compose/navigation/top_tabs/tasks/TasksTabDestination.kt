package work.racka.reluct.android.compose.navigation.top_tabs.tasks

import work.racka.reluct.common.core_navigation.compose_destinations.tasks.CompletedTasksDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.PendingTasksDestination
import work.racka.reluct.common.core_navigation.compose_destinations.tasks.TasksStatisticsDestination

enum class TasksTabDestination(
    val route: String
) {
    Tasks(
        route = PendingTasksDestination.route,
    ),
    Done(
        route = CompletedTasksDestination.route,
    ),
    Statistics(
        route = TasksStatisticsDestination.route,
    );
}