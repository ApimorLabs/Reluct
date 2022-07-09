package work.racka.reluct.android.compose.navigation.top_tabs.tasks

import work.racka.reluct.android.compose.navigation.destinations.tasks.CompletedTasksDestination
import work.racka.reluct.android.compose.navigation.destinations.tasks.PendingTasksDestination
import work.racka.reluct.android.compose.navigation.destinations.tasks.TasksStatisticsDestination

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