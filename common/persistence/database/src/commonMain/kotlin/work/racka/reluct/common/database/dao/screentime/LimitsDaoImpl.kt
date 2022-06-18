package work.racka.reluct.common.database.dao.screentime

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.dao.screentime.LimitsHelpers.getPausedAppsFromDb
import work.racka.reluct.common.database.dao.screentime.LimitsHelpers.insertAppToDb
import work.racka.reluct.common.database.models.LimitsDbObject

internal class LimitsDaoImpl(
    private val dispatcher: CoroutineDispatcher,
    databaseWrapper: DatabaseWrapper
) : LimitsDao {

    private val limitsQueries = databaseWrapper.instance?.limitsTableQueries

    override suspend fun insertApp(appLimit: LimitsDbObject) {
        limitsQueries?.insertAppToDb(limit = appLimit)
    }

    override suspend fun removeApp(packageName: String) {
        limitsQueries?.transaction {
            limitsQueries.removeApp(packageName = packageName)
        }
    }

    override suspend fun removeAllApps() {
        limitsQueries?.transaction {
            limitsQueries.removeAllApps()
        }
    }

    override suspend fun setTimeLimit(packageName: String, timeLimit: Long) {
        limitsQueries?.transaction {
            limitsQueries.setTimeLimit(
                timeLimit = timeLimit,
                packageName = packageName
            )
        }
    }

    override suspend fun togglePausedApp(packageName: String, isPaused: Boolean) {
        limitsQueries?.transaction {
            limitsQueries.togglePausedApp(
                isPaused = isPaused,
                packageName = packageName
            )
        }
    }

    override suspend fun toggleDistractingApp(packageName: String, isDistracting: Boolean) {
        limitsQueries?.transaction {
            limitsQueries.toggleDistractingApp(
                isDistracting = isDistracting,
                packageName = packageName
            )
        }
    }

    override suspend fun getPausedApps(): Flow<List<LimitsDbObject>> =
        limitsQueries?.getPausedAppsFromDb()
            ?.asFlow()
            ?.mapToList(dispatcher)
            ?: flowOf(emptyList())

    override suspend fun getPausedAppsSync(): List<LimitsDbObject> =
        limitsQueries?.getPausedAppsFromDb()?.executeAsList() ?: emptyList()

    override suspend fun isAppPaused(packageName: String): Boolean =
        limitsQueries?.isAppPaused(packageName = packageName)
            ?.executeAsOne()
            ?: false

    override suspend fun resumeAllApp() {
        limitsQueries?.transaction {
            limitsQueries.removeAllApps()
        }
    }
}