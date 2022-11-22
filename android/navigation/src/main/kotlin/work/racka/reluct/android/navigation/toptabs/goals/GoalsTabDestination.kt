package work.racka.reluct.android.navigation.toptabs.goals

import work.racka.reluct.common.core.navigation.composeDestinations.goals.ActiveGoalsDestination
import work.racka.reluct.common.core.navigation.composeDestinations.goals.InactiveGoalsDestination

enum class GoalsTabDestination(
    val route: String
) {
    Active(
        route = work.racka.reluct.common.core.navigation.composeDestinations.goals.ActiveGoalsDestination.route
    ),
    Inactive(
        route = work.racka.reluct.common.core.navigation.composeDestinations.goals.InactiveGoalsDestination.route
    );
}
