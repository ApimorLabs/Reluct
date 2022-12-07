package work.racka.reluct.common.database.sync

interface DbNetworkSync {
    // Tasks
    fun syncTasksData(userId: String)
    fun stopSyncTasksData()
}
