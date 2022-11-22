package work.racka.reluct.compose.common.components.cards.statistics.piechart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import work.racka.reluct.compose.common.charts.pieChart.PieChart
import work.racka.reluct.compose.common.charts.pieChart.PieChartData
import work.racka.reluct.compose.common.charts.pieChart.renderer.slice.SimpleSliceDrawer
import work.racka.reluct.compose.common.charts.pieChart.renderer.text.SimpleTextDrawer

@Composable
fun StatisticsPieChartBase(
    slices: ImmutableList<PieChartData.Slice>,
    dataLoading: Boolean,
    middleText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    chartSize: Dp = 160.dp,
) {
    Box(
        modifier = modifier then Modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        if (dataLoading) {
            CircularProgressIndicator()
        } else {
            PieChart(
                modifier = Modifier.size(chartSize),
                pieChartData = PieChartData(slices = slices, spacingBy = .05f),
                sliceDrawer = SimpleSliceDrawer(sliceThickness = 15f),
                centerTextDrawer = SimpleTextDrawer(labelTextColor = contentColor),
                centerText = middleText,
                onCenterClick = onClick
            )
        }
    }
}
