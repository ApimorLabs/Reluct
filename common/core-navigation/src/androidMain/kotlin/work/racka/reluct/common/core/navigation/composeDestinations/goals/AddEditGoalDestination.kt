package work.racka.reluct.common.core.navigation.composeDestinations.goals

import androidx.navigation.*
import work.racka.reluct.common.core.navigation.destination.NavDefaults
import work.racka.reluct.common.core.navigation.destination.NavDestination

object AddEditGoalDestination :
    NavDestination {
    private const val APP_URI = NavDefaults.APP_DESTINATION_URI
    private const val BASE_LINK = "add_edit_goal"

    override val route: String =
        "$BASE_LINK-route/{${AddEditGoalArgs.GoalId.name}}/{${AddEditGoalArgs.DefaultGoalIndex.name}}"
    override val destination: String = "$BASE_LINK-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(AddEditGoalArgs.GoalId.name) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(
                AddEditGoalArgs.DefaultGoalIndex.name
            ) {
                type = NavType.IntType
                defaultValue = -1
            }
        )

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern = "$APP_URI/$route"
            }
        )

    fun argsRoute(goalId: String?, defaultGoalIndex: Int?) =
        "$BASE_LINK-route/$goalId/${defaultGoalIndex ?: -1}"

    fun addEditGoalDeeplink(goalId: String?, defaultGoalIndex: Int?) =
        "$APP_URI/${argsRoute(goalId, defaultGoalIndex)}"
}

enum class AddEditGoalArgs {
    GoalId, DefaultGoalIndex;
}
