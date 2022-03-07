package work.racka.thinkrchive.v2.common.database.di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import org.koin.dsl.module
import work.racka.thinkrchive.v2.common.database.dao.ThinkrchiveDatabaseWrapper
import work.racka.thinkrchive.v2.common.database.db.ThinkpadDatabase

internal actual object Platform {
    actual fun platformDatabaseModule() = module {
        single {
            val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
                .also { ThinkpadDatabase.Schema.create(it) }
            ThinkrchiveDatabaseWrapper(ThinkpadDatabase(driver))
        }
    }
}
