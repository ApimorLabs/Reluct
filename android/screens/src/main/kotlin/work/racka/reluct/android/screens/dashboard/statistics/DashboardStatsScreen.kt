package work.racka.reluct.android.screens.dashboard.statistics

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.common.features.dashboard.statistics.DashboardStatisticsViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun DashboardStatsScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
    onNavigateToScreenTimeStats: () -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }

    val viewModel: DashboardStatisticsViewModel = getCommonViewModel()
    val screenTimeUiState by viewModel.screenTimeUiState.collectAsStateWithLifecycle()
    val tasksStatsUiState by viewModel.tasksStatsUiState.collectAsStateWithLifecycle()

    DashboardStatsUI(
        mainScaffoldPadding = mainScaffoldPadding,
        barsVisibility = barsVisibility,
        snackbarState = snackbarState,
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
