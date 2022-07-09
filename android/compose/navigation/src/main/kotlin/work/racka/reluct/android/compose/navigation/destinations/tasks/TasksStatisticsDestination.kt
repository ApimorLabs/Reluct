package work.racka.reluct.android.compose.navigation.destinations.tasks

import work.racka.reluct.common.core_navigation.destination.NavDestination

object TasksStatisticsDestination : NavDestination {
    private const val BASE_URI = "tasks_statistics"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}