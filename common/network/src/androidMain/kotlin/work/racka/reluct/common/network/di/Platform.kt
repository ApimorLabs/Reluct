package work.racka.reluct.common.network.di

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.dsl.module
import work.racka.reluct.common.network.sync.DbNetworkSync
import work.racka.reluct.common.network.sync.FirebaseNetworkSyncService
import work.racka.reluct.common.network.sync.tasks.FirebaseTasksUpload
import work.racka.reluct.common.network.sync.tasks.TasksUpload
import work.racka.reluct.common.network.util.Constants

actual object Platform {
    actual fun module(): Module = module {
        single<DbNetworkSync> {
            FirebaseNetworkSyncService(
                database = Firebase.database(Constants.FIREBASE_DEFAULT_DB_URL),
                tasksDao = get()
            )
        }

        factory<TasksUpload> {
            FirebaseTasksUpload(
                database = Firebase.database(Constants.FIREBASE_DEFAULT_DB_URL),
                dispatcher = Dispatchers.IO
            )
        }
    }
}
