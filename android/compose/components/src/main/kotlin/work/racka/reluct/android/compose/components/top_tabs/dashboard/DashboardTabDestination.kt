package work.racka.reluct.android.compose.components.top_tabs.dashboard

import androidx.annotation.StringRes
import work.racka.reluct.android.compose.components.R


enum class DashboardTabDestination(
    @StringRes val titleResId: Int
) {
    Overview(titleResId = R.string.dashboard_overview),
    Statistics(titleResId = R.string.dashboard_statistics);
}