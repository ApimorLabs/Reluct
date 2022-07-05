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
                    isPaused = limit.isPaused,
                    overridden = limit.overridden
                )
            )
        }
    }

    fun LimitsTableQueries.getPausedAppsFromDb() =
        this.getPausedApps { packageName, timeLimit, isADistractingApp, isPaused, overridden ->
            limitsDbObjectMapper(
                packageName = packageName,
                timeLimit = timeLimit,
                isADistractingApp = isADistractingApp,
                isPaused = isPaused,
                overridden = overridden
            )
        }

    fun LimitsTableQueries.getDistractingAppsFromDb() =
        this.getDistractingApps { packageName, timeLimit, isADistractingApp, isPaused, overridden ->
            limitsDbObjectMapper(
                packageName = packageName,
                timeLimit = timeLimit,
                isADistractingApp = isADistractingApp,
                isPaused = isPaused,
                overridden = overridden
            )
        }

    fun LimitsTableQueries.getAppFromDb(packageName: String) =
        this.getApp(packageName) { packageName, timeLimit, isADistractingApp, isPaused, overridden ->
            limitsDbObjectMapper(
                packageName = packageName,
                timeLimit = timeLimit,
                isADistractingApp = isADistractingApp,
                isPaused = isPaused,
                overridden = overridden
            )
        }

    private fun limitsDbObjectMapper(
        packageName: String,
        timeLimit: Long,
        isADistractingApp: Boolean,
        isPaused: Boolean,
        overridden: Boolean
    ): LimitsDbObject = LimitsDbObject(
        packageName = packageName,
        timeLimit = timeLimit,
        isADistractingAp = isADistractingApp,
        isPaused = isPaused,
        overridden = overridden
    )
}