package work.racka.reluct.android.screens.screentime.app_stats_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.HourglassBottom
import androidx.compose.material.icons.rounded.PauseCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.buttons.ValueOffsetButton
import work.racka.reluct.android.compose.components.cards.headers.ListGroupHeadingHeader
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.cards.statistics.screen_time.AppScreenTimeStatisticsCard
import work.racka.reluct.android.compose.components.dialogs.CircularProgressDialog
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.AppNameEntry
import work.racka.reluct.android.screens.screentime.components.AppTimeLimitDialog
import work.racka.reluct.android.screens.screentime.components.LimitsDetailsCard
import work.racka.reluct.android.screens.screentime.components.LimitsSwitchCard
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppScreenTimeStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.AppSettingsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screen_time.statistics.states.app_stats.WeeklyAppUsageStatsState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AppScreenTimeStatsUI(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    uiState: AppScreenTimeStatsState,
    toggleDistractingState: (value: Boolean) -> Unit,
    togglePausedState: (value: Boolean) -> Unit,
    saveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (offset: Int) -> Unit,
    goBack: () -> Unit
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
        topBar = {
            TopAppBar(
                title = {
                    TopAppInfoItem(
                        modifier = Modifier.padding(bottom = Dimens.SmallPadding.size),
                        dailyData = uiState.dailyData
                    )
                },
                modifier = Modifier.statusBarsPadding(),
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = appBarColors
            )
        },
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
                        onBarClicked = { onSelectDay(it) }
                    ) {
                        ValueOffsetButton(
                            text = uiState.selectedInfo.selectedWeekText,
                            offsetValue = uiState.selectedInfo.weekOffset,
                            onOffsetValueChange = { onUpdateWeekOffset(it) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            shape = Shapes.large,
                            incrementEnabled = uiState.selectedInfo.weekOffset < 0
                        )
                    }
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
                                onClick = { showAppTimeLimitDialog = true },
                                icon = Icons.Rounded.HourglassBottom
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

                        // Pause App
                        item {
                            LimitsSwitchCard(
                                title = stringResource(R.string.manually_pause_app),
                                description = stringResource(R.string.manually_pause_app_desc),
                                checked = appSettings.isPaused,
                                onCheckedChange = { togglePausedState(it) },
                                icon = Icons.Rounded.PauseCircle
                            )
                        }
                    }
                    else -> {
                        item {
                            LinearProgressIndicator()
                        }
                    }
                }

                // Bottom Space
                item {
                    Spacer(
                        modifier = Modifier
                            .navigationBarsPadding()
                            .height(padding.calculateBottomPadding())
                    )
                }
            }
        }
    }

    // App Time Limit Dialog
    if (showAppTimeLimitDialog) {
        when (val appSettings = uiState.appSettingsState) {
            is AppSettingsState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = { showAppTimeLimitDialog = false },
                    initialAppTimeLimit = appSettings.appTimeLimit,
                    onSaveTimeLimit = saveTimeLimit
                )
            }
            else -> {
                CircularProgressDialog(
                    onDismiss = { showAppTimeLimitDialog = false },
                    loadingText = stringResource(id = R.string.loading_text)
                )
            }
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

    Box(
        modifier = Modifier.height(Dimens.ExtraLargePadding.size),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = appName != null && appIcon != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            AppNameEntry(
                modifier = modifier,
                appName = appName!!,
                icon = appIcon!!,
                textStyle = MaterialTheme.typography.headlineSmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private val appBarColors: TopAppBarColors
    @Composable
    get() = TopAppBarDefaults
        .smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = LocalContentColor.current,
            titleContentColor = LocalContentColor.current,
            actionIconContentColor = LocalContentColor.current
        )