package work.racka.reluct.common.network.sync

internal class DesktopNetworkSync : DbNetworkSync {
    override fun syncTasksData() {
        println("Sync Tasks not implemented!")
    }

    override fun stopSyncTasksData() {
        println("Stop Syncing Tasks!")
    }
}
