package work.racka.reluct.compose.common.charts.pieChart

import androidx.compose.ui.graphics.Color
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PieChartData(
    val slices: ImmutableList<Slice>,
    val spacingBy: Float = 0f
) {
    init {
        require(spacingBy in 0f..1f)
    }

    internal val totalSize: Float
        get() {
            var total = 0f
            slices.forEach { total += it.value }
            val emptySliceLength = (total / slices.size) * spacingBy
            return total + (emptySliceLength * slices.size)
        }

    internal val slicesWithSpacing: ImmutableList<Slice>
        get() {
            val emptySliceLength = (totalSize / slices.size) * spacingBy
            val mutableSlices = persistentListOf<Slice>().builder().apply {
                slices.forEach {
                    add(it)
                    add(Slice(emptySliceLength, Color.Transparent))
                }
            }.build()
            return mutableSlices
        }

    data class Slice(
        val value: Float,
        val color: Color
    )
}