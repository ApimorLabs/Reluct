package work.racka.reluct.common.core_navigation.compose_destinations

import work.racka.reluct.common.core_navigation.destination.NavDestination

object OtherDestination : NavDestination {
    private const val BASE_URI = "other_graph"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}