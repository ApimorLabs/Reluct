package work.racka.reluct.common.database.dao.screentime

import work.racka.reluct.common.database.models.LimitsDbObject
import work.racka.reluct.common.database.tables.LimitsTable
import work.racka.reluct.common.database.tables.LimitsTableQueries

object LimitsHelpers {
    fun LimitsTableQueries.insertAppToDb(limit: LimitsDbObject) {
        this.transaction {
            insertApp(
                LimitsTable(
                    packageName = limit.packageName,
                    timeLimit = limit.timeLimit,
                    isADistractingApp = limit.isADistractingAp,
                    isPaused = limit.isPaused
                )
            )
        }
    }

    fun LimitsTableQueries.getPausedAppsFromDb() =
        this.getPausedApps { packageName, timeLimit, isADistractingApp, isPaused ->
            limitsDbObjectMapper(
                packageName = packageName,
                timeLimit = timeLimit,
                isADistractingApp = isADistractingApp,
                isPaused = isPaused
            )
        }

    private fun limitsDbObjectMapper(
        packageName: String, timeLimit: Long, isADistractingApp: Boolean, isPaused: Boolean
    ): LimitsDbObject = LimitsDbObject(
        packageName = packageName,
        timeLimit = timeLimit,
        isADistractingAp = isADistractingApp,
        isPaused = isPaused
    )
}