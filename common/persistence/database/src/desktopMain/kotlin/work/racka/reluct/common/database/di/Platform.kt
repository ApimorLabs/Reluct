package work.racka.reluct.common.database.di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import work.racka.reluct.common.database.ReluctDatabase
import work.racka.reluct.common.database.adapters.GoalsTableAdapter
import work.racka.reluct.common.database.adapters.TasksTableAdapter
import work.racka.reluct.common.database.dao.DatabaseWrapper
import work.racka.reluct.common.database.util.Constants
import java.io.File

internal actual object Platform {
    actual fun platformDatabaseModule() = module {
        single {
            val dbPath = File(System.getProperty("java.io.tmpdir"), Constants.RELUCT_DATABASE)
            val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${dbPath.absolutePath}")
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
