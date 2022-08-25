package work.racka.reluct.android.compose.navigation.top_tabs.dashboard

import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardOverviewDestination
import work.racka.reluct.common.core_navigation.compose_destinations.dashboard.DashboardStatsDestination

enum class DashboardTabDestination(
    val route: String
) {
    Overview(
        route = DashboardOverviewDestination.route
    ),
    Statistics(
        route = DashboardStatsDestination.route
    );
}