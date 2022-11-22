package work.racka.reluct.common.core.navigation.composeDestinations.goals

import work.racka.reluct.common.core.navigation.destination.NavDestination

object InactiveGoalsDestination :
    NavDestination {
    private const val BASE_URI = "inactive_goals"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}
