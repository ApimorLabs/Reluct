package work.racka.reluct.common.core_navigation.compose_destinations.goals

import work.racka.reluct.common.core_navigation.destination.NavDestination

object InactiveGoalsDestination : NavDestination {
    private const val BASE_URI = "inactive_goals"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}