package work.racka.reluct.android.compose.navigation.toptabs.goals

import work.racka.reluct.common.core_navigation.compose_destinations.goals.ActiveGoalsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.InactiveGoalsDestination

enum class GoalsTabDestination(
    val route: String
) {
    Active(
        route = ActiveGoalsDestination.route
    ),
    Inactive(
        route = InactiveGoalsDestination.route
    );
}
