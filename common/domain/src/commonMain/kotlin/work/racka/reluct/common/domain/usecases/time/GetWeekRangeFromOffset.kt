package work.racka.reluct.common.domain.usecases.time

interface GetWeekRangeFromOffset {
    suspend fun invoke(weekOffset: Int): String
}
