package work.racka.reluct.android.compose.components.cards.statistics.piechart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.statistics.ChartData
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.util.extractColor
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.pieChart.PieChartData

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DailyScreenTimePieChart(
    pieChartState: StatisticsChartState<UsageStats>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    chartSize: Dp = 160.dp
) {
    val chartSlices by produceState(initialValue = ChartData(), pieChartState) {
        value = withContext(Dispatchers.IO) {
            val data = persistentListOf<PieChartData.Slice>().builder().apply {
                val list = pieChartState.data.appsUsageList
                val firstItems = list.take(4)
                val otherItems = list - firstItems.toSet()
                val otherSlice = PieChartData.Slice(
                    value = otherItems.sumOf { it.timeInForeground }.toFloat(),
                    color = Color.Gray
                )
                firstItems.forEach { data ->
                    val colorInt = data.appIcon.icon.extractColor()
                    val slice = PieChartData.Slice(
                        value = data.timeInForeground.toFloat(),
                        color = Color(colorInt)
                    )
                    add(slice)
                }
                add(otherSlice)
            }.build().toImmutableList()
            ChartData(
                data = data,
                isLoading = pieChartState is StatisticsChartState.Loading
            )
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape),
        onClick = onClick
    ) {
        AnimatedContent(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            targetState = chartSlices.isLoading,
            contentAlignment = Alignment.Center
        ) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .height(chartSize)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator()
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pie Chart
                    StatisticsPieChartBase(
                        modifier = Modifier,
                        slices = chartSlices.data,
                        contentColor = contentColor,
                        dataLoading = chartSlices.isLoading,
                        middleText = "",
                        onClick = onClick,
                        chartSize = chartSize
                    )
                    Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                    // Left Text
                    StatsDetails(
                        modifier = Modifier,
                        contentColor = contentColor,
                        screenTimeText = pieChartState.data.formattedTotalScreenTime,
                        unlockCount = if (chartSlices.isLoading) {
                            "..."
                        } else {
                            pieChartState.data.unlockCount.toString()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsDetails(
    contentColor: Color,
    screenTimeText: String,
    unlockCount: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement
            .spacedBy(Dimens.SmallPadding.size)
    ) {
        Text(
            text = stringResource(id = R.string.screen_time_text),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )
        Text(
            text = screenTimeText,
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier)

        Text(
            text = stringResource(id = R.string.unlocks_text),
            style = MaterialTheme.typography.bodyMedium
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor
        )
        Text(
            text = unlockCount,
            style = MaterialTheme.typography.titleLarge
                .copy(fontWeight = FontWeight.Medium),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
