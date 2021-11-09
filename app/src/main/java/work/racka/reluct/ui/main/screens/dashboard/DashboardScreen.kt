package work.racka.reluct.ui.main.screens.dashboard

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import work.racka.reluct.ui.components.Greeting
import work.racka.reluct.ui.components.PermCheck
import work.racka.reluct.ui.components.UsageStat
import work.racka.reluct.ui.components.charts.barChart.BarChart
import work.racka.reluct.ui.components.charts.barChart.BarChartData
import work.racka.reluct.ui.components.charts.barChart.BarChartOptions
import work.racka.reluct.ui.components.charts.barChart.renderer.label.SimpleValueDrawer
import work.racka.reluct.ui.components.charts.barChart.renderer.xaxis.SimpleXAxisDrawer
import work.racka.reluct.ui.components.charts.barChart.renderer.yaxis.SimpleYAxisDrawer
import work.racka.reluct.ui.components.charts.pieChart.PieChart
import work.racka.reluct.ui.components.charts.pieChart.PieChartData
import work.racka.reluct.ui.components.charts.pieChart.renderer.slice.SimpleSliceDrawer
import work.racka.reluct.ui.components.charts.pieChart.renderer.text.SimpleTextDrawer
import work.racka.reluct.ui.main.states.StatsState
import work.racka.reluct.ui.main.viewmodels.UsageDataViewModel
import work.racka.reluct.ui.theme.Dimens
import work.racka.reluct.utils.Utils
import java.util.concurrent.TimeUnit

@Composable
fun DashboardScreen(

) {
    // A surface container using the 'background' color from the theme
    val context = LocalContext.current
    val isGranted by remember {
        mutableStateOf(
            Utils.checkUsageAccessPermissions(context)
        )
    }

    val viewModel: UsageDataViewModel = hiltViewModel()
    val uiState by viewModel.uIState.collectAsState()
    val data = uiState as StatsState.Stats

    if (uiState is StatsState.Stats && data.usageStats.isNotEmpty()) {

        val chartData by remember(data.usageStats) {
            mutableStateOf(data.usageStats)
        }

        Surface(color = MaterialTheme.colorScheme.background) {
            LazyColumn(
                state = rememberLazyListState(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Greeting("Compose")
                    Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                    PermCheck(isGranted = isGranted)
                    Spacer(modifier = Modifier.height(Dimens.MediumPadding.size))
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                        context.startActivity(intent)
                    }) {
                        Text(text = "Request")
                    }
                }

                item {
                    val slices = mutableListOf<PieChartData.Slice>()
                    var otherTime = 0L
                    data.dayStats.appsUsageList.forEach { appUsageInfo ->
                        if (appUsageInfo.timeInForeground >=
                            TimeUnit.MINUTES.toMillis(15L)
                        ) {
                            slices.add(
                                PieChartData.Slice(
                                    value = appUsageInfo.timeInForeground.toFloat(),
                                    color = Color(appUsageInfo.dominantColor)
                                )
                            )
                        } else {
                            otherTime += appUsageInfo.timeInForeground
                        }
                    }

                    slices.add(
                        PieChartData.Slice(otherTime.toFloat(), Color.Gray)
                    )

                    PieChart(
                        pieChartData = PieChartData(slices, 0.05f),
                        modifier = Modifier.size(size = 200.dp),
                        sliceDrawer = SimpleSliceDrawer(10f),
                        centerTextDrawer = SimpleTextDrawer(
                            labelTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        centerText = Utils.getFormattedTime(
                            data.dayStats.totalScreenTime
                        )
                    )
                }

                item {
                    val list = mutableListOf<BarChartData.Bar>()
                    chartData.forEach { stats ->
                        list.add(
                            BarChartData.Bar(
                                value = stats.totalScreenTime.toFloat(),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                label = stats.dayOfWeek.day,
                                uniqueId = stats.dayOfWeek.value
                            )
                        )
                    }
                    BarChart(
                        barChartData = BarChartData(
                            bars = list
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(size = 160.dp)
                            .padding(Dimens.MediumPadding.size),
                        selectedUniqueId = data.selectedDay,
                        selectedBarColor = MaterialTheme.colorScheme.primary,
                        onBarClicked = {
                            viewModel.updateDailyAppUsageInfo(it)
                        },
                        xAxisDrawer = SimpleXAxisDrawer(
                            axisLineColor = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0.5f)
                        ),
                        yAxisDrawer = SimpleYAxisDrawer(
                            labelValueFormatter = { value ->
                                val hr = TimeUnit.MILLISECONDS.toHours(value.toLong())
                                "$hr h"
                            },
                            labelTextColor = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0.5f),
                            axisLineColor = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0.5f)
                        ),
                        labelDrawer = SimpleValueDrawer(
                            labelTextColor = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0.5f)
                        ),
                        barChartOptions = BarChartOptions().apply {
                            barsSpacingFactor = 0.05f
                        }
                    )
                }

                item {
                    Text(
                        text = data.dayStats.unlockCount.toString(),
                        modifier = Modifier
                            .padding(Dimens.MediumPadding.size),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                items(data.dayStats.appsUsageList) {
                    UsageStat(
                        appInfo = it
                    )
                }
            }
        }
    }
}