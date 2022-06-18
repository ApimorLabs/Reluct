package work.racka.reluct.common.database.dao.screentime

import kotlinx.coroutines.flow.Flow
import work.racka.reluct.common.database.models.LimitsDbObject

interface LimitsDao {
    /**
     * Inserts an app limit object into the database
     * This should rarely be used because the specific functions
     * each limit can already create an Limits object when the
     * given packageName is absent
     */
    suspend fun insertApp(appLimit: LimitsDbObject)

    /**
     * It will remove the specified [packageName] from the Limits table
     * Useful from clearing apps that are no longer present in the device
     */
    suspend fun removeApp(packageName: String)

    /**
     * It will remove all the apps from the Limits table
     * Useful from clearing apps
     */
    suspend fun removeAllApps()

    /**
     * Sets the [timeLimit] for the specified [packageName]
     * If the [packageName] does not exist in the database it will be created
     */
    suspend fun setTimeLimit(packageName: String, timeLimit: Long)

    /**
     * Toggles an app's paused state by the provided [isPaused] value for the
     * specified [packageName]
     * If the [packageName] does not exist in the database it will be created
     */
    suspend fun togglePausedApp(packageName: String, isPaused: Boolean)

    /**
     * Toggles an app's distracting state by the provided [isDistracting] value for the
     * specified [packageName]. Useful in Focus Mode
     * If the [packageName] does not exist in the database it will be created
     */
    suspend fun toggleDistractingApp(packageName: String, isDistracting: Boolean)

    /**
     * Get a list of the paused apps asynchronously using [Flow]
     */
    suspend fun getPausedApps(): Flow<List<LimitsDbObject>>

    /**
     * Get a list of the paused apps synchronously using [Flow]
     * To be used in places where collecting [Flow] is not ideal
     */
    suspend fun getPausedAppsSync(): List<LimitsDbObject>

    /**
     * Check if an app of the specified [packageName] is in Paused state
     */
    suspend fun isAppPaused(packageName: String): Boolean

    /**
     * Resume all app that were in paused state
     */
    suspend fun resumeAllApp()
}