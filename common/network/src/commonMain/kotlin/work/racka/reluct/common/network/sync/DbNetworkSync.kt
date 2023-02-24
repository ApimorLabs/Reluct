package work.racka.reluct.common.network.sync

interface DbNetworkSync {
    // Tasks
    fun syncTasksData()
    fun stopSyncTasksData()
}
