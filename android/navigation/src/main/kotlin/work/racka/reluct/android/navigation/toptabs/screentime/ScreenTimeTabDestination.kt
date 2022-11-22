package work.racka.reluct.android.navigation.toptabs.screentime

import work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeLimitsDestination
import work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeStatsDestination

enum class ScreenTimeTabDestination(
    val route: String
) {
    Statistics(
        route = work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeStatsDestination.route
    ),
    Limits(
        route = work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeLimitsDestination.route
    );
}
