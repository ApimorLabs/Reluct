package work.racka.reluct.compose.common.components.cards.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.compose.common.charts.barChart.BarChartData
import work.racka.reluct.compose.common.theme.Dimens
import work.racka.reluct.compose.common.theme.Shapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatisticsBarChartCard(
    bars: ImmutableList<BarChartData.Bar>,
    dataLoading: Boolean,
    noDataText: String,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    topLeftText: @Composable () -> Unit,
    topRightText: @Composable () -> Unit,
    belowChartText: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    shape: Shape = Shapes.large,
    selectedBarColor: Color = contentColor,
    chartHeight: Dp = 160.dp,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement
                .spacedBy(Dimens.MediumPadding.size)
        ) {
            // Top Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement
                    .spacedBy(Dimens.MediumPadding.size)
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart,
                    content = { topLeftText() }
                )

                Box(
                    contentAlignment = Alignment.CenterEnd,
                    content = { topRightText() }
                )
            }

            // Chart Area
            Box(contentAlignment = Alignment.Center) {
                val sumOfBarsValue = bars.sumOf { it.value.toDouble() }
                StatisticsBarChartBase(
                    modifier = Modifier.height(chartHeight),
                    bars = bars,
                    selectedBarColor = selectedBarColor,
                    selectedDayIsoNumber = selectedDayIsoNumber,
                    onBarClicked = { onBarClicked(it) }
                )
                if (dataLoading) {
                    LinearProgressIndicator()
                } else if (sumOfBarsValue.equals(0.0)) {
                    Text(
                        text = noDataText,
                        style = MaterialTheme.typography.titleLarge
                            .copy(fontWeight = FontWeight.Medium),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColor
                    )
                }
            }

            // Text Below Chart for stats info
            belowChartText()

            // Body Content
            bodyContent()
        }
    }
}
