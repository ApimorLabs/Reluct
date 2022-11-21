package work.racka.reluct.android.compose.components.cards.statistics

sealed class StatisticsChartState<T>(val data: T) {
    class Success<T>(data: T) : StatisticsChartState<T>(data = data)
    class Loading<T>(data: T) : StatisticsChartState<T>(data = data)
    class Empty<T>(data: T) : StatisticsChartState<T>(data = data)
}
