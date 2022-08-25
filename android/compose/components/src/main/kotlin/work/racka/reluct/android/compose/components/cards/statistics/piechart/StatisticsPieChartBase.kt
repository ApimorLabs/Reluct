package work.racka.reluct.android.compose.components.cards.statistics.piechart

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
import work.racka.reluct.pieChart.PieChart
import work.racka.reluct.pieChart.PieChartData
import work.racka.reluct.pieChart.renderer.slice.SimpleSliceDrawer
import work.racka.reluct.pieChart.renderer.text.SimpleTextDrawer

@Composable
fun StatisticsPieChartBase(
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    slices: List<PieChartData.Slice>,
    dataLoading: Boolean,
    middleText: String,
    chartSize: Dp = 100.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier.size(chartSize) then modifier,
        contentAlignment = Alignment.Center
    ) {
        if (dataLoading) {
            CircularProgressIndicator()
        } else {
            PieChart(
                modifier = Modifier.size(chartSize),
                pieChartData = PieChartData(slices = slices, spacingBy = .05f),
                sliceDrawer = SimpleSliceDrawer(sliceThickness = 10f),
                centerTextDrawer = SimpleTextDrawer(labelTextColor = contentColor),
                centerText = middleText,
                onCenterClick = onClick
            )
        }
    }
}