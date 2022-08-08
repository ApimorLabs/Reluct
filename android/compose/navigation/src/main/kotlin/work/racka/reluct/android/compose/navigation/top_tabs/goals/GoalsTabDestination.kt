package work.racka.reluct.android.compose.navigation.top_tabs.goals

import work.racka.reluct.common.core_navigation.compose_destinations.goals.CompletedGoalsDestination
import work.racka.reluct.common.core_navigation.compose_destinations.goals.OngoingGoalsDestination

enum class GoalsTabDestination(
    val route: String
) {
    Ongoing(
        route = OngoingGoalsDestination.route
    ),
    Completed(
        route = CompletedGoalsDestination.route
    );
}