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
     * Gets A Flow of saved App Limit from the database.
     * If not present it returns a default value.
     */
    fun getApp(packageName: String): Flow<LimitsDbObject>

    /**
     * Gets A saved App Limit from the database synchronously
     */
    suspend fun getAppSync(packageName: String): LimitsDbObject

    /**
     * Get all Distracting Apps. Return an empty flow list when nothing is found
     */
    fun getDistractingApps(): Flow<List<LimitsDbObject>>

    fun getDistractingAppsSync(): List<LimitsDbObject>

    /**
     * Check if the given app's [packageName] is marked as a Distracting app
     */
    suspend fun isDistractingApp(packageName: String): Boolean

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
     * Overrides app paused state by the provided [overridden] value for the
     * specified [packageName]. Used by user to override paused state activated when a
     * limit is reached
     */
    suspend fun toggleLimitOverride(packageName: String, overridden: Boolean)

    /**
     * Get a list of the paused apps asynchronously using [Flow]
     */
    fun getPausedApps(): Flow<List<LimitsDbObject>>

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
    suspend fun resumeAllApps()
}