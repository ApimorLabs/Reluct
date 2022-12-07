package work.racka.reluct.common.network.di

import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.network.sync.DbNetworkSync
import work.racka.reluct.common.network.sync.DesktopNetworkSync
import work.racka.reluct.common.network.sync.tasks.DesktopTasksUpload
import work.racka.reluct.common.network.sync.tasks.TasksUpload

actual object Platform {
    actual fun module(): Module = module {
        single<DbNetworkSync> {
            DesktopNetworkSync()
        }

        factory<TasksUpload> { DesktopTasksUpload() }
    }
}
