package work.racka.reluct.compose.common.components.cards.statistics.piechart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.compose.common.charts.pieChart.PieChartData
import work.racka.reluct.compose.common.components.SharedRes
import work.racka.reluct.compose.common.components.cards.statistics.ChartData
import work.racka.reluct.compose.common.components.resources.stringResource
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DailyScreenTimePieChart(
    chartData: State<ChartData<PieChartData.Slice>>,
    unlockCountProvider: () -> Long,
    screenTimeTextProvider: () -> String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    chartSize: Dp = 160.dp
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .clip(shape),
        onClick = onClick
    ) {
        AnimatedContent(
            modifier = Modifier.padding(Dimens.MediumPadding.size),
            targetState = chartData.value.isLoading,
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
                        slices = chartData.value.data,
                        contentColor = contentColor,
                        dataLoading = chartData.value.isLoading,
                        middleText = "",
                        onClick = onClick,
                        chartSize = chartSize
                    )
                    Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
                    // Left Text
                    StatsDetails(
                        modifier = Modifier,
                        contentColor = contentColor,
                        screenTimeText = screenTimeTextProvider(),
                        unlockCount = if (chartData.value.isLoading) "..." else unlockCountProvider().toString()
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
            text = stringResource(SharedRes.strings.screen_time_text),
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
            text = stringResource(SharedRes.strings.unlocks_text),
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
