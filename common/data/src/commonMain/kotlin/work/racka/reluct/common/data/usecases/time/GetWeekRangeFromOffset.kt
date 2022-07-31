package work.racka.reluct.common.data.usecases.time

interface GetWeekRangeFromOffset {
    suspend fun invoke(weekOffset: Int): String
}