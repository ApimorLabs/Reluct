package work.racka.reluct.android.screens.screentime.app_stats_details

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AppBlocking
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppSettingsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.WeeklyAppUsageStatsState

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppScreenTimeStatsUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: AppScreenTimeStatsState,
    toggleDistractingState: (value: Boolean) -> Unit,
    saveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    selectDay: (dayIsoNumber: Int) -> Unit,
    updateWeekOffset: (offset: Int) -> Unit
) {

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

    val listState = rememberLazyListState()
    var showAppTimeLimitDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {},
        snackbarHost = {
            SnackbarHost(hostState = it) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    backgroundColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
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
                modifier = Modifier.fillMaxSize(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Space
                item {
                    Spacer(modifier = Modifier)
                }
                // App Settings Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.app_settings_header))
                }

                when (val appSettings = uiState.appSettingsState) {
                    is AppSettingsState.Data -> {
                        // App Timer
                        item {
                            LimitsDetailsCard(
                                title = stringResource(R.string.app_time_limit),
                                description = appSettings.appTimeLimit.formattedTime,
                                onClick = { showAppTimeLimitDialog = true }
                            )
                        }

                        // Distracting App
                        item {
                            LimitsSwitchCard(
                                title = stringResource(R.string.distracting_app),
                                description = stringResource(R.string.distracting_app_desc),
                                checked = appSettings.isDistractingApp,
                                onCheckedChange = { toggleDistractingState(it) },
                                icon = Icons.Rounded.AppBlocking
                            )
                        }
                    }
                    else -> {
                        item {
                            LinearProgressIndicator()
                        }
                    }
                }
            }
        }
    }

    // App Time Limit Dialog
    if (showAppTimeLimitDialog) {
        /*AppTimeLimitDialog(
            onDismiss = { showAppTimeLimitDialog = false },
            appTimeLimitState = uiState.appTimeLimit,
            onSaveTimeLimit = onSaveAppTimeLimitSettings
        )*/
    }
}