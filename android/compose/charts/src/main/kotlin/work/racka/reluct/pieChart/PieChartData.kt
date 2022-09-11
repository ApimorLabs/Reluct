package work.racka.reluct.pieChart

import androidx.compose.ui.graphics.Color

data class PieChartData(
    val slices: List<Slice>,
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

    internal val slicesWithSpacing: List<Slice>
        get() {
            val mutableSlices = mutableListOf<Slice>()
            val emptySliceLength = (totalSize / slices.size) * spacingBy
            slices.forEach {
                mutableSlices.add(it)
                mutableSlices.add(
                    Slice(emptySliceLength, Color.Transparent)
                )
            }
            return mutableSlices
        }

    data class Slice(
        val value: Float,
        val color: Color
    )
}