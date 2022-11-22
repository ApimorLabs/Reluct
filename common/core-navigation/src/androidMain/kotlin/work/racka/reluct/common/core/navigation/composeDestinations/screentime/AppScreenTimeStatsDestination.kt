package work.racka.reluct.common.core.navigation.composeDestinations.screentime

import androidx.navigation.*
import work.racka.reluct.common.core.navigation.destination.NavDefaults
import work.racka.reluct.common.core.navigation.destination.NavDestination

object AppScreenTimeStatsDestination :
    NavDestination {
    private const val APP_URI = NavDefaults.APP_DESTINATION_URI
    private const val BASE_LINK = "screen_time_stats_app_info"
    override val route: String = "$BASE_LINK-route/{${AppScreenTimeStatsArgs.PackageName.name}}"
    override val destination: String = "$BASE_LINK-destination"

    override val args: List<NamedNavArgument>
        get() = listOf(
            navArgument(AppScreenTimeStatsArgs.PackageName.name) {
                type = NavType.StringType
            }
        )

    override val deepLinks: List<NavDeepLink>
        get() = listOf(
            navDeepLink {
                uriPattern =
                    "$APP_URI/screen_time_stats/{${AppScreenTimeStatsArgs.PackageName.name}}"
            }
        )

    fun appScreenTimeDeepLink(packageName: String) = "$APP_URI/screen_time_stats/$packageName"

    fun argsRoute(packageName: String) = "$BASE_LINK-route/$packageName"
}

enum class AppScreenTimeStatsArgs {
    PackageName;
}
