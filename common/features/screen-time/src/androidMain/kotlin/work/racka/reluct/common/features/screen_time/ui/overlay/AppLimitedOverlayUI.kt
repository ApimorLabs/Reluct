package work.racka.reluct.common.features.screen_time.ui.overlay

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.screen_time.AppScreenTimeStatisticsCard
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.common.features.screen_time.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.WeeklyAppUsageStatsState

@Composable
internal fun AppLimitedOverlayUI(viewModel: AppScreenTimeStatsViewModel, exit: () -> Unit) {

    val uiState by viewModel.uiState.collectAsState()

    val barChartState = remember(uiState.weeklyData) {
        derivedStateOf {
            val result = when (val weeklyState = uiState.weeklyData) {
                is WeeklyAppUsageStatsState.Data -> {
                    StatisticsChartState.Success(data = weeklyState.usageStats)
                }
                is WeeklyAppUsageStatsState.Loading -> {
                    StatisticsChartState.Loading(data = weeklyState.usageStats)
                }
                is WeeklyAppUsageStatsState.Empty -> {
                    StatisticsChartState.Empty(data = weeklyState.usageStats)
                }
                else -> {
                    StatisticsChartState.Empty(data = weeklyState.usageStats)
                }
            }
            result
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(Dimens.MediumPadding.size),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape),
                    onClick = exit,
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onPrimary,
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = .9f),
    ) { padding ->
        Box(
            modifier = Modifier
                .animateContentSize()
                .padding(padding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Chart
                val dailyData = uiState.dailyData
                item {
                    AppScreenTimeStatisticsCard(
                        barChartState = barChartState.value,
                        selectedDayText = if (dailyData is DailyAppUsageStatsState.Data)
                            dailyData.dayText else "...",
                        selectedDayScreenTime = if (dailyData is DailyAppUsageStatsState.Data)
                            dailyData.usageStat.appUsageInfo.formattedTimeInForeground else "...",
                        weeklyTotalScreenTime = uiState.weeklyData.formattedTotalTime,
                        selectedDayIsoNumber = uiState.selectedInfo.selectedDay,
                        onBarClicked = { viewModel.selectDay(it) },
                        weekUpdateButton = { /** No Button **/ }
                    )
                }
            }
        }
    }
}