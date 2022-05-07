package work.racka.reluct.common.features.tasks.usecases.interfaces

interface GetWeekRangeFromOffset {
    operator fun invoke(weekOffset: Int): String
}