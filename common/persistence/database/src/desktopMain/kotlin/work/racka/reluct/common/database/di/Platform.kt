package work.racka.reluct.common.database.di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import work.racka.reluct.common.database.ReluctDatabase
import work.racka.reluct.common.database.adapters.GoalsTableAdapter
import work.racka.reluct.common.database.adapters.TasksTableAdapter
import work.racka.reluct.common.database.dao.DatabaseWrapper

internal actual object Platform {
    actual fun platformDatabaseModule() = module {
        single {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { ReluctDatabase.Schema.create(it) }
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
