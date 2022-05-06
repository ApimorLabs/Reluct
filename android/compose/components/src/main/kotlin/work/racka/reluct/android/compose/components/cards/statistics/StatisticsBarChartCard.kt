package work.racka.reluct.android.compose.components.cards.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.barChart.BarChartData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StatisticsBarChartCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    shape: Shape = Shapes.large,
    bars: List<BarChartData.Bar>,
    dataLoading: Boolean,
    chartHeight: Dp = 160.dp,
    selectedDayIsoNumber: Int,
    onBarClicked: (Int) -> Unit,
    topLeftText: @Composable () -> Unit,
    topRightText: @Composable () -> Unit,
    belowChartText: @Composable () -> Unit,
    bodyContent: @Composable () -> Unit,
) {
    Card(
        containerColor = containerColor,
        contentColor = contentColor,
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
                    selectedDayIsoNumber = selectedDayIsoNumber,
                    onBarClicked = { onBarClicked(it) }
                )
                if (dataLoading) {
                    LinearProgressIndicator()
                } else if (sumOfBarsValue.equals(0.0) && !dataLoading) {
                    Text(
                        text = stringResource(id = R.string.no_data_text),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = LocalContentColor.current
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