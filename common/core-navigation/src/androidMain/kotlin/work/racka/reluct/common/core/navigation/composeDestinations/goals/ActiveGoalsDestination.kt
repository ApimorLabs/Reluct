package work.racka.reluct.common.core.navigation.composeDestinations.goals

import work.racka.reluct.common.core.navigation.destination.NavDestination

object ActiveGoalsDestination :
    NavDestination {
    private const val BASE_URI = "active_goals"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}
