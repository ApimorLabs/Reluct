package work.racka.reluct.android.compose.components.cards.statistics.piechart

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import work.racka.reluct.android.compose.components.R
import work.racka.reluct.android.compose.components.cards.statistics.StatisticsChartState
import work.racka.reluct.android.compose.components.util.extractColor
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.compose.theme.Shapes
import work.racka.reluct.common.model.domain.usagestats.UsageStats
import work.racka.reluct.pieChart.PieChartData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyScreenTimePieChart(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    pieChartState: StatisticsChartState<UsageStats>,
    onClick: () -> Unit,
    shape: Shape = Shapes.large,
) {
    val slices by remember(pieChartState) {
        derivedStateOf {
            val tempList = mutableListOf<PieChartData.Slice>()
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
                tempList.add(slice)
            }
            tempList.add(otherSlice)
            tempList.toList()
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
        Row(
            modifier = Modifier
                .padding(Dimens.MediumPadding.size)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pie Chart
            StatisticsPieChartBase(
                modifier = Modifier,
                slices = slices,
                contentColor = contentColor,
                dataLoading = pieChartState is StatisticsChartState.Loading,
                middleText = "",
                onClick = onClick
            )
            Spacer(modifier = Modifier.width(Dimens.MediumPadding.size))
            // Left Text
            StatsDetails(
                modifier = Modifier,
                contentColor = contentColor,
                screenTimeText = pieChartState.data.formattedTotalScreenTime,
                unlockCount = if (pieChartState is StatisticsChartState.Loading) "..."
                else pieChartState.data.unlockCount.toString()
            )
        }
    }
}

@Composable
private fun StatsDetails(
    modifier: Modifier = Modifier,
    contentColor: Color,
    screenTimeText: String,
    unlockCount: String
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