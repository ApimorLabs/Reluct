package work.racka.reluct.android.compose.navigation.destinations.tasks

import work.racka.reluct.common.core_navigation.destination.NavDestination

object PendingTasksDestination : NavDestination {
    private const val BASE_URI = "pending_tasks"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}