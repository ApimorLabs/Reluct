package work.racka.reluct.ui.components.charts.pieChart

import androidx.compose.ui.graphics.Color
import timber.log.Timber

data class PieChartData(
    val slices: List<Slice>,
    val spacing: Float = 5f
) {
    internal val totalSize: Float
        get() {
            var total = 0f
            slices.forEach { total += it.value }
            val emptySliceLength = (total / slices.size) * (spacing / 100f)
            Timber.d("Slice Total Length Before: $total")
            Timber.d("Slice Total length after: ${total + (emptySliceLength * slices.size)}")
            return total + (emptySliceLength * slices.size)
        }

    internal val slicesWithSpacing: List<Slice>
        get() {
            val mutableSlices = mutableListOf<Slice>()
            val emptySliceLength = (totalSize / slices.size) * (spacing / 100f)
            slices.forEach {
                mutableSlices.add(it)
                mutableSlices.add(
                    Slice(emptySliceLength, Color.Transparent)
                )
            }
            Timber.d("Slice w/ Spaces: ${mutableSlices.size}")
            Timber.d("Slice Empty length: $emptySliceLength")
            return mutableSlices
        }

    data class Slice(
        val value: Float,
        val color: Color
    )
}