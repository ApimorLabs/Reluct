package work.racka.reluct.common.database.dao.screentime

import work.racka.reluct.common.database.models.LimitsDbObject
import work.racka.reluct.common.database.tables.LimitsTable
import work.racka.reluct.common.database.tables.LimitsTableQueries

internal object LimitsHelpers {
    fun LimitsTableQueries.insertAppToDb(limit: LimitsDbObject) {
        transaction {
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

    fun LimitsTableQueries.getPausedAppsFromDb() = getPausedApps(mapper = limitsDbObjectMapper)

    fun LimitsTableQueries.getDistractingAppsFromDb() =
        getDistractingApps(mapper = limitsDbObjectMapper)

    fun LimitsTableQueries.getAppFromDb(packageName: String) =
        getApp(packageName, mapper = limitsDbObjectMapper)

    private val limitsDbObjectMapper: (
        packageName: String,
        timeLimit: Long,
        isADistractingApp: Boolean,
        isPaused: Boolean,
        overridden: Boolean
    ) -> LimitsDbObject = { packageName, timeLimit, isADistractingApp, isPaused, overridden ->
        LimitsDbObject(
            packageName = packageName,
            timeLimit = timeLimit,
            isADistractingAp = isADistractingApp,
            isPaused = isPaused,
            overridden = overridden
        )
    }
}
