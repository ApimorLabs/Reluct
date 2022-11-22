package work.racka.reluct.android.navigation.toptabs.screentime

import work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeLimitsDestination
import work.racka.reluct.common.core.navigation.composeDestinations.screentime.ScreenTimeStatsDestination

enum class ScreenTimeTabDestination(val route: String) {
    Statistics(route = ScreenTimeStatsDestination.route),
    Limits(route = ScreenTimeLimitsDestination.route);
}
