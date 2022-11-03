package work.racka.reluct.common.features.screen_time.ui.overlay

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.android.compose.components.buttons.ReluctButton
import work.racka.reluct.android.compose.components.cards.card_with_actions.ReluctDescriptionCard
import work.racka.reluct.android.compose.components.cards.statistics.BarChartDefaults
import work.racka.reluct.android.compose.components.cards.statistics.ChartData
import work.racka.reluct.android.compose.components.cards.statistics.screen_time.ScreenTimeStatisticsCard
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.barChart.BarChartData
import work.racka.reluct.common.features.screen_time.R
import work.racka.reluct.common.features.screen_time.statistics.AppScreenTimeStatsViewModel
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppSettingsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.WeeklyAppUsageStatsState
import work.racka.reluct.common.features.screen_time.ui.components.AppNameEntry
import work.racka.reluct.common.model.domain.usagestats.AppUsageStats
import work.racka.reluct.common.model.util.time.Week

@Composable
internal fun AppLimitedOverlayUI(
    viewModel: AppScreenTimeStatsViewModel,
    focusModeOn: Boolean,
    exit: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    val barColor = BarChartDefaults.barColor
    val barChartData by produceState(
        initialValue = ChartData(
            isLoading = uiState.weeklyData is WeeklyAppUsageStatsState.Loading
        ),
        uiState.weeklyData
    ) {
        value = ChartData(
            data = getWeeklyAppScreenTimeChartData(uiState.weeklyData.usageStats, barColor),
            isLoading = uiState.weeklyData is WeeklyAppUsageStatsState.Loading
        )
    }

    val appSettingsData by remember(uiState.appSettingsState) {
        derivedStateOf {
            if (uiState.appSettingsState is AppSettingsState.Data)
                uiState.appSettingsState as AppSettingsState.Data else null
        }
    }

    val dailyDataState by remember(uiState.dailyData) {
        derivedStateOf {
            if (uiState.dailyData is DailyAppUsageStatsState.Data)
                uiState.dailyData as DailyAppUsageStatsState.Data else null
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = .9f)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(Dimens.MediumPadding.size)
                .statusBarsPadding()
                //.navigationBarsPadding()
                .shadow(elevation = 6.dp, shape = Shapes.large)
                .background(MaterialTheme.colorScheme.background, Shapes.large),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(Dimens.MediumPadding.size),
            verticalArrangement = Arrangement.spacedBy(Dimens.SmallPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // App Name
            item {
                TopAppInfoItem(
                    modifier = Modifier.fillMaxWidth(),
                    dailyData = uiState.dailyData
                )
            }

            item {
                ReasonCard(
                    dailyDataState = dailyDataState,
                    appSettingsData = appSettingsData,
                    focusModeOn = focusModeOn
                )
            }

            // Chart
            val dailyData = uiState.dailyData
            item {
                ScreenTimeStatisticsCard(
                    chartData = barChartData,
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

            item {
                ReluctButton(
                    modifier = Modifier.fillMaxWidth(.5f),
                    buttonText = stringResource(id = R.string.exit_text),
                    icon = Icons.Rounded.ExitToApp,
                    onButtonClicked = exit
                )
            }
        }

        // Close Button
        IconButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(end = Dimens.MediumPadding.size)
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            onClick = exit,
        ) {
            Icon(
                tint = MaterialTheme.colorScheme.onPrimary,
                imageVector = Icons.Rounded.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TopAppInfoItem(
    modifier: Modifier = Modifier,
    dailyData: DailyAppUsageStatsState
) {
    val appInfo by remember(dailyData) {
        derivedStateOf {
            if (dailyData is DailyAppUsageStatsState.Data) dailyData.usageStat.appUsageInfo
            else null
        }
    }

    /**
     * This all done to prevent flicker when new data is selected
     * Obtaining these directly from [dailyData] will cause multiple recomposition with
     * evaluation that will flicker the component.
     */
    val appIcon by remember(appInfo?.appName) { derivedStateOf { appInfo?.appIcon?.icon } }
    val appName by remember(appInfo?.appIcon) { derivedStateOf { appInfo?.appName } }

    if (appName != null && appIcon != null) {
        AppNameEntry(
            modifier = modifier,
            appName = appName!!,
            icon = appIcon!!,
            contentColor = MaterialTheme.colorScheme.onBackground,
            textStyle = MaterialTheme.typography.headlineSmall
        )
    } else {
        Text(
            modifier = modifier,
            text = "• • • • •",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ReasonCard(
    modifier: Modifier = Modifier,
    dailyDataState: DailyAppUsageStatsState.Data?,
    appSettingsData: AppSettingsState.Data?,
    focusModeOn: Boolean
) {

    if (appSettingsData != null) {
        if (dailyDataState != null) {
            if (dailyDataState.usageStat.appUsageInfo.timeInForeground
                >= appSettingsData.appTimeLimit.timeInMillis
                && appSettingsData.appTimeLimit.timeInMillis != 0L
            ) {
                // Show Time Limit Exceeded
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "Exceeded Screen Time Limit",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "Your Limit: ${appSettingsData.appTimeLimit.formattedTime}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.HourglassBottom,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            } else if (focusModeOn && appSettingsData.isDistractingApp) {
                // Show Focus Mode
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "Focus Mode Is On",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "This is a Distracting App. Exit to focus more!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.AppBlocking,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            } else {
                ReluctDescriptionCard(
                    modifier = modifier,
                    title = {
                        Text(
                            text = "App Paused",
                            style = MaterialTheme.typography.titleLarge,
                            color = LocalContentColor.current
                        )
                    },
                    description = {
                        Text(
                            text = "App has been paused for the rest of the day!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalContentColor.current.copy(alpha = .8f)
                        )
                    },
                    leftItems = {
                        Icon(
                            imageVector = Icons.Rounded.PauseCircle,
                            contentDescription = null
                        )
                    },
                    onClick = { }
                )
            }
        }
    }
}

private suspend inline fun getWeeklyAppScreenTimeChartData(
    weeklyStats: ImmutableMap<Week, AppUsageStats>,
    barColor: Color
) = withContext(Dispatchers.IO) {
    persistentListOf<BarChartData.Bar>().builder().apply {
        for (key in weeklyStats.keys) {
            val data = weeklyStats.getValue(key)
            add(
                BarChartData.Bar(
                    value = data.appUsageInfo.timeInForeground.toFloat(),
                    color = barColor,
                    label = key.dayAcronym,
                    uniqueId = key.isoDayNumber
                )
            )
        }
    }.build().toImmutableList()
}
