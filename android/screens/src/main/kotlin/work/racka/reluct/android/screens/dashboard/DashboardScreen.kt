package work.racka.reluct.android.screens.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.android.compose.components.top_tabs.dashboard.DashboardTabDestination
import work.racka.reluct.android.compose.components.util.BarsVisibility
import work.racka.reluct.android.screens.dashboard.components.DashboardScreenTopBar
import work.racka.reluct.android.screens.dashboard.overview.DashboardOverviewUI
import work.racka.reluct.android.screens.dashboard.statistics.DashboardStatsUI
import work.racka.reluct.common.features.dashboard.combined.DashboardViewModel

@OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun DashboardScreen(
    mainScaffoldPadding: PaddingValues,
    barsVisibility: BarsVisibility,
    onNavigateToScreenTime: () -> Unit,
    onNavigateToTaskDetails: (taskId: String) -> Unit,
    onNavigateToGoalDetails: (goalId: String) -> Unit,
    onNavigateToAppUsageInfo: (packageName: String) -> Unit,
    onSettingsClicked: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = DashboardTabDestination.Overview.ordinal)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val mainViewModel = getCommonViewModel<DashboardViewModel>()
    // UI States
    val overviewUiState by mainViewModel.overview.uiState.collectAsStateWithLifecycle()
    val screenTimeUiState by mainViewModel.screenTimeStats.uiState.collectAsStateWithLifecycle()
    val tasksUiState by mainViewModel.tasksStats.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == DashboardTabDestination.Overview.ordinal) {
            mainViewModel.overview.initializeData()
        } else {
            mainViewModel.screenTimeStats.initializeData()
            mainViewModel.tasksStats.initializeData()
            mainViewModel.screenTimeStats.permissionCheck(true)
        }
    }

    Scaffold(
        topBar = {
            DashboardScreenTopBar(
                pagerState = pagerState,
                updateTabPage = {
                    scope.launch { pagerState.animateScrollToPage(it.ordinal) }
                },
                onSettingsClicked = onSettingsClicked
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        HorizontalPager(
            modifier = Modifier.padding(innerPadding),
            count = DashboardTabDestination.values().size,
            state = pagerState
        ) { page ->
            when (page) {
                DashboardTabDestination.Overview.ordinal -> {

                    DashboardOverviewUI(
                        mainScaffoldPadding = mainScaffoldPadding,
                        barsVisibility = barsVisibility,
                        snackbarHostState = snackbarHostState,
                        uiState = overviewUiState,
                        getUsageData = mainViewModel.overview::permissionCheck,
                        openScreenTimeStats = onNavigateToScreenTime,
                        openPendingTask = { onNavigateToTaskDetails(it.id) },
                        onToggleTaskDone = mainViewModel.overview::toggleDone,
                        onGoalClicked = { onNavigateToGoalDetails(it.id) }
                    )
                }
                DashboardTabDestination.Statistics.ordinal -> {

                    DashboardStatsUI(
                        mainScaffoldPadding = mainScaffoldPadding,
                        barsVisibility = barsVisibility,
                        snackbarState = snackbarHostState,
                        screenTimeUiState = screenTimeUiState,
                        tasksStatsUiState = tasksUiState,
                        onTasksSelectDay = mainViewModel.tasksStats::selectDay,
                        onScreenTimeSelectDay = mainViewModel.screenTimeStats::selectDay,
                        onSelectAppTimeLimit = mainViewModel.screenTimeStats::selectAppTimeLimit,
                        onSaveTimeLimit = mainViewModel.screenTimeStats::saveTimeLimit,
                        onAppUsageInfoClick = { onNavigateToAppUsageInfo(it.packageName) },
                        onViewAllScreenTimeStats = onNavigateToScreenTime
                    )
                }
            }
        }
    }
}