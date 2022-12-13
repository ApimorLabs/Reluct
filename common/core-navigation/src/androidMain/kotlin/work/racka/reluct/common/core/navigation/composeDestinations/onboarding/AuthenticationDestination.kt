package work.racka.reluct.common.core.navigation.composeDestinations.onboarding

import work.racka.reluct.common.core.navigation.destination.NavDestination

object AuthenticationDestination : NavDestination {
    private const val BASE_URI = "authentication"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"
}
