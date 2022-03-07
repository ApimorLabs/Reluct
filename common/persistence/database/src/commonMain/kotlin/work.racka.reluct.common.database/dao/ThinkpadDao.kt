package work.racka.thinkrchive.v2.common.database.dao

import data.local.ThinkpadDatabaseObject
import data.remote.response.ThinkpadResponse
import kotlinx.coroutines.flow.Flow

interface ThinkpadDao {
    fun insertAllThinkpads(response: List<ThinkpadResponse>)
    fun getAllThinkpads(): Flow<List<ThinkpadDatabaseObject>>
    fun getThinkpad(thinkpadModel: String): Flow<ThinkpadDatabaseObject>?
    fun getThinkpadsAlphaAscending(thinkpadModel: String): Flow<List<ThinkpadDatabaseObject>>
    fun getThinkpadsNewestFirst(thinkpadModel: String): Flow<List<ThinkpadDatabaseObject>>
    fun getThinkpadsOldestFirst(thinkpadModel: String): Flow<List<ThinkpadDatabaseObject>>
    fun getThinkpadsLowPriceFirst(thinkpadModel: String): Flow<List<ThinkpadDatabaseObject>>
    fun getThinkpadsHighPriceFirst(thinkpadModel: String): Flow<List<ThinkpadDatabaseObject>>
}