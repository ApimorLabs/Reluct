package work.racka.reluct.common.database.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.dsl.module
import work.racka.reluct.common.database.ReluctDatabase
import work.racka.reluct.common.database.adapters.GoalsTableAdapter
import work.racka.reluct.common.database.adapters.TasksTableAdapter
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.util.Constants

internal actual object Platform {
    actual fun platformDatabaseModule() = module {
        single {
            val driver = AndroidSqliteDriver(
                schema = ReluctDatabase.Schema,
                context = get(),
                name = Constants.RELUCT_DATABASE
            )
            DatabaseWrapper(
                ReluctDatabase(
                    driver,
                    GoalsTableAdapter = GoalsTableAdapter,
                    TasksTableAdapter = TasksTableAdapter
                )
            )
        }
    }
}
