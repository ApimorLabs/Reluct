package work.racka.reluct.common.core_navigation.compose_destinations.onboarding

import work.racka.reluct.common.core_navigation.destination.NavDestination

object OnBoardingDestination : NavDestination {
    private const val BASE_URI = "on_boarding"
    override val route: String = "$BASE_URI-route"
    override val destination: String = "$BASE_URI-destination"

}