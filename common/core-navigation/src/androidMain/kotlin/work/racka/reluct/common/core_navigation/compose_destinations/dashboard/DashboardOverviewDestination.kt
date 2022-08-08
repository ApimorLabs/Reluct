package work.racka.reluct.common.core_navigation.compose_destinations.dashboard

import work.racka.reluct.common.core_navigation.destination.NavDestination

object DashboardOverviewDestination : NavDestination {
    private const val BASE_URI = "dashboard_overview"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}