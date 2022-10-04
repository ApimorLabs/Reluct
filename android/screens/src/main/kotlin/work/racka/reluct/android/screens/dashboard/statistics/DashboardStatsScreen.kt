package work.racka.reluct.android.screens.dashboard.statistics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.common.features.dashboard.statistics.DashboardStatisticsViewModel

@Composable
fun DashboardStatsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
    onNavigateToScreenTimeStats: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    val viewModel: DashboardStatisticsViewModel = getCommonViewModel()
    val screenTimeUiState by viewModel.screenTimeUiState.collectAsState()
    val tasksStatsUiState by viewModel.tasksStatsUiState.collectAsState()

    DashboardStatsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        scaffoldState = scaffoldState,
        screenTimeUiState = screenTimeUiState,
        tasksStatsUiState = tasksStatsUiState,
        onTasksSelectDay = { viewModel.tasksSelectDay(it) },
        onScreenTimeSelectDay = { viewModel.screenTimeSelectDay(it) },
        onSelectAppTimeLimit = { viewModel.selectAppTimeLimit(it) },
        onSaveTimeLimit = { hours, minutes -> viewModel.saveAppTimeLimit(hours, minutes) },
        onAppUsageInfoClick = { onNavigateToAppUsageInfo(it.packageName) },
        onViewAllScreenTimeStats = onNavigateToScreenTimeStats
    )
}