package work.racka.reluct.common.data.usecases.app_usage

interface GetDailyUsageStatsUseCase {
    operator fun invoke(weekOffset: Int = 0, dayIsoNumber: Int)
}