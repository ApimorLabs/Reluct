package work.racka.reluct.common.database.sync

internal class DesktopNetworkSync : DbNetworkSync {
    override fun syncTasksData(userId: String) {
        println("Sync Tasks for user id: $userId")
    }

    override fun stopSyncTasksData() {
        println("Stop Syncing Tasks!")
    }
}
