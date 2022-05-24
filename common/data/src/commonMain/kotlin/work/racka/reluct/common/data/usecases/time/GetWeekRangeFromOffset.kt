package work.racka.reluct.common.data.usecases.time

interface GetWeekRangeFromOffset {
    operator fun invoke(weekOffset: Int): String
}