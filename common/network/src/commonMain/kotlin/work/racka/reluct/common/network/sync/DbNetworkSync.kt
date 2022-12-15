package work.racka.reluct.common.network.sync

interface DbNetworkSync {
    // Tasks
    fun syncTasksData(userId: String)
    fun stopSyncTasksData()
}
