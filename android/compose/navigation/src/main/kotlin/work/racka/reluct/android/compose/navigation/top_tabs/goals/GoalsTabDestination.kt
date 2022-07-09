package work.racka.reluct.android.compose.navigation.top_tabs.goals

import work.racka.reluct.android.compose.navigation.destinations.goals.CompletedGoalsDestination
import work.racka.reluct.android.compose.navigation.destinations.goals.OngoingGoalsDestination

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