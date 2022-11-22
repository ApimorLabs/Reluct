package work.racka.reluct.common.core.navigation.composeDestinations

import work.racka.reluct.common.core.navigation.destination.NavDestination

object OtherDestination : NavDestination {
    private const val BASE_URI = "other_graph"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}
