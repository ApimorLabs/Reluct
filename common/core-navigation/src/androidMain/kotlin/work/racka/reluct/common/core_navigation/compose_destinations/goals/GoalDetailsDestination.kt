package work.racka.reluct.common.core_navigation.compose_destinations.goals

import androidx.navigation.*
import work.racka.reluct.common.core_navigation.destination.NavDefaults
import work.racka.reluct.common.core_navigation.destination.NavDestination

object GoalDetailsDestination : NavDestination {
    private const val APP_URI = NavDefaults.APP_DESTINATION_URI
    private const val BASE_LINK = "goal_details"

    override val route: String =
        "$BASE_LINK-route/{${GoalDetailsArgs.GoalId.name}}"
    override val destination: String = "$BASE_LINK-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(GoalDetailsArgs.GoalId.name) {
                type = NavType.StringType
                nullable = true
            }
        )

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern = "$APP_URI/$route"
            }
        )

    fun argsRoute(goalId: String?) = "$BASE_LINK-route/$goalId"

    fun goalDetailsDeeplink(goalId: String?) = "$APP_URI/${argsRoute(goalId)}"
}

enum class GoalDetailsArgs {
    GoalId;
}