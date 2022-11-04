package work.racka.reluct.android.screens.screentime.appStatsDetails

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import work.racka.reluct.android.compose.components.cards.statistics.BarChartDefaults
import work.racka.reluct.android.compose.components.cards.statistics.screenTime.ScreenTimeStatisticsCard
import work.racka.reluct.android.compose.components.dialogs.CircularProgressDialog
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.android.screens.R
import work.racka.reluct.android.screens.screentime.components.*
import work.racka.reluct.common.features.screenTime.statistics.states.ScreenTimeStatsSelectedInfo
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.AppScreenTimeStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.AppSettingsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.DailyAppUsageStatsState
import work.racka.reluct.common.features.screenTime.statistics.states.appStats.WeeklyAppUsageStatsState

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun AppScreenTimeStatsUI(
    snackbarHostState: SnackbarHostState,
    uiState: State<AppScreenTimeStatsState>,
    toggleDistractingState: (value: Boolean) -> Unit,
    togglePausedState: (value: Boolean) -> Unit,
    saveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onSelectDay: (dayIsoNumber: Int) -> Unit,
    onUpdateWeekOffset: (offset: Int) -> Unit,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val barColor = BarChartDefaults.barColor
    val barChartData = getWeeklyAppScreenTimeChartData(
        weeklyStatsProvider = { uiState.value.weeklyData.usageStats },
        isLoadingProvider = { uiState.value.weeklyData is WeeklyAppUsageStatsState.Loading },
        barColor = barColor
    )

    val listState = rememberLazyListState()
    val showAppTimeLimitDialog = remember { mutableStateOf(false) }

    println("App Screen Time UI Composed")
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    TopAppInfoItem(
                        modifier = Modifier.padding(bottom = Dimens.SmallPadding.size),
                        dailyData = uiState.value.dailyData
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
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier.navigationBarsPadding(),
                    shape = RoundedCornerShape(10.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        println("App Screen Time Scaffold Composed")
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
                item {
                    ScreenTimeStatisticsCard(
                        chartData = barChartData,
                        selectedDayText = {
                            when (val data = uiState.value.dailyData) {
                                is DailyAppUsageStatsState.Data -> data.dayText
                                else -> "..."
                            }
                        },
                        selectedDayScreenTime = {
                            when (val data = uiState.value.dailyData) {
                                is DailyAppUsageStatsState.Data -> {
                                    data.usageStat.appUsageInfo.formattedTimeInForeground
                                }
                                else -> "..."
                            }
                        },
                        weeklyTotalScreenTime = { uiState.value.weeklyData.formattedTotalTime },
                        selectedDayIsoNumber = { uiState.value.selectedInfo.selectedDay },
                        onBarClicked = { onSelectDay(it) },
                        weekUpdateButton = {
                            WeekSelectorButton(
                                selectedInfoProvider = { uiState.value.selectedInfo },
                                onUpdateWeekOffset = onUpdateWeekOffset
                            )
                        }
                    )
                }

                // App Settings Header
                stickyHeader {
                    ListGroupHeadingHeader(text = stringResource(R.string.app_settings_header))
                }

                // App Extra Actions
                appExtraConfiguration(
                    getAppSettingsState = { uiState.value.appSettingsState },
                    onShowAppTimeLimitDialog = { showAppTimeLimitDialog.value = true },
                    onToggleDistractingState = toggleDistractingState,
                    onTogglePausedState = togglePausedState
                )

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
    ShowAppTimeLimitDialog(
        openDialog = showAppTimeLimitDialog,
        appSettingsStateProvider = { uiState.value.appSettingsState },
        onSaveTimeLimit = saveTimeLimit,
        onClose = { showAppTimeLimitDialog.value = false }
    )
}


private fun LazyListScope.appExtraConfiguration(
    getAppSettingsState: () -> AppSettingsState,
    onShowAppTimeLimitDialog: (Boolean) -> Unit,
    onToggleDistractingState: (Boolean) -> Unit,
    onTogglePausedState: (Boolean) -> Unit
) {
    when (val appSettings = getAppSettingsState()) {
        is AppSettingsState.Data -> {
            // App Timer
            item {
                LimitsDetailsCard(
                    title = stringResource(R.string.app_time_limit),
                    description = appSettings.appTimeLimit.formattedTime,
                    onClick = { onShowAppTimeLimitDialog(true) },
                    icon = Icons.Rounded.HourglassBottom
                )
            }

            // Distracting App
            item {
                LimitsSwitchCard(
                    title = stringResource(R.string.distracting_app),
                    description = stringResource(R.string.distracting_app_desc),
                    checked = appSettings.isDistractingApp,
                    onCheckedChange = onToggleDistractingState,
                    icon = Icons.Rounded.AppBlocking
                )
            }

            // Pause App
            item {
                LimitsSwitchCard(
                    title = stringResource(R.string.manually_pause_app),
                    description = stringResource(R.string.manually_pause_app_desc),
                    checked = appSettings.isPaused,
                    onCheckedChange = onTogglePausedState,
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
}

@Composable
private fun WeekSelectorButton(
    selectedInfoProvider: () -> ScreenTimeStatsSelectedInfo,
    onUpdateWeekOffset: (offset: Int) -> Unit
) {
    val selectedInfo = remember { derivedStateOf { selectedInfoProvider() }}
    ValueOffsetButton(
        text = selectedInfo.value.selectedWeekText,
        offsetValue = selectedInfo.value.weekOffset,
        onOffsetValueChange = onUpdateWeekOffset,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = Shapes.large,
        incrementEnabled = selectedInfo.value.weekOffset < 0
    )
}

@Composable
private fun ShowAppTimeLimitDialog(
    openDialog: State<Boolean>,
    appSettingsStateProvider: () -> AppSettingsState,
    onSaveTimeLimit: (hours: Int, minutes: Int) -> Unit,
    onClose: () -> Unit,
) {
    if (openDialog.value) {
        when (val appSettings = appSettingsStateProvider()) {
            is AppSettingsState.Data -> {
                AppTimeLimitDialog(
                    onDismiss = onClose,
                    initialAppTimeLimit = appSettings.appTimeLimit,
                    onSaveTimeLimit = onSaveTimeLimit
                )
            }
            else -> {
                CircularProgressDialog(
                    onDismiss = onClose,
                    loadingText = stringResource(id = R.string.loading_text)
                )
            }
        }
    }
}

@Composable
private fun TopAppInfoItem(
    dailyData: DailyAppUsageStatsState,
    modifier: Modifier = Modifier,
) {
    val appInfo by remember(dailyData) {
        derivedStateOf {
            if (dailyData is DailyAppUsageStatsState.Data) {
                dailyData.usageStat.appUsageInfo
            } else {
                null
            }
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
