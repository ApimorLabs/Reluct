package work.racka.reluct.ui.components.charts.barChart

data class BarChartOptions(
    var drawXAxisLine: Boolean = true,
    var drawYAxisLine: Boolean = false,
    var showXAxisLabels: Boolean = true,
    var showYAxisLabels: Boolean = true,
    var showIntervalLines: Boolean = true,
    var barsSpacingFactor: Float = 0.1f
)
