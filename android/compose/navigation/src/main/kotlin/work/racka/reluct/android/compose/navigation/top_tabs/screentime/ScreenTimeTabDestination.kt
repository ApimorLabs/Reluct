package work.racka.reluct.android.compose.navigation.top_tabs.screentime

import work.racka.reluct.android.compose.navigation.destinations.screentime.ScreenTimeLimitsDestination
import work.racka.reluct.android.compose.navigation.destinations.screentime.ScreenTimeStatsDestination

enum class ScreenTimeTabDestination(
    val route: String
) {
    Statistics(
        route = ScreenTimeStatsDestination.route
    ),
    Limits(
        route = ScreenTimeLimitsDestination.route
    );
}