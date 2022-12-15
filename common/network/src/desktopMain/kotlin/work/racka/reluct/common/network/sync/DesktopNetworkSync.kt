package work.racka.reluct.common.network.sync

internal class DesktopNetworkSync : DbNetworkSync {
    override fun syncTasksData(userId: String) {
        println("Sync Tasks for user id: $userId")
    }

    override fun stopSyncTasksData() {
        println("Stop Syncing Tasks!")
    }
}
