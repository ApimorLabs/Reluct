package work.racka.reluct.common.core_navigation.compose_destinations.tasks

import androidx.navigation.*
import work.racka.reluct.common.core_navigation.destination.NavDefaults
import work.racka.reluct.common.core_navigation.destination.NavDestination

object TaskDetailsDestination : NavDestination {
    private const val APP_URI = NavDefaults.APP_DESTINATION_URI
    private const val BASE_LINK = "task_details"
    override val route: String = "$BASE_LINK-route/{${AddEditTaskArgs.TaskId.name}}"
    override val destination: String = "$BASE_LINK-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(AddEditTaskArgs.TaskId.name) {
                type = NavType.StringType
            }
        )

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern = "$APP_URI/tasks/{${AddEditTaskArgs.TaskId.name}}"
            }
        )

    fun taskDetailsDeepLink(taskId: String?) = "$APP_URI/tasks/$taskId"

    fun argsRoute(taskId: String?) = "$BASE_LINK-route/$taskId"
}

enum class TaskDetailsArgs {
    TaskId;
}