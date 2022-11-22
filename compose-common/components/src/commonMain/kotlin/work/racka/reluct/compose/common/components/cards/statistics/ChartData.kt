package work.racka.reluct.compose.common.components.cards.statistics

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

/**
 * Data for the charts. Should only be used as Compose state
 * @param data List that contains the Slices or Bars to be drawn on the chart
 * @param isLoading Loading state of the chart. When it is loading, we expect the [data] to empty
 */
@Stable
data class ChartData<T>(
    val data: ImmutableList<T> = persistentListOf(),
    val isLoading: Boolean = true
)
