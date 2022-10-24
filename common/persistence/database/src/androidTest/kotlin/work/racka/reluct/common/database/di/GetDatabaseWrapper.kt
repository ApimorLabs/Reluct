package work.racka.reluct.common.database.di

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import work.racka.reluct.common.database.ReluctDatabase
import work.racka.reluct.common.database.adapters.GoalsTableAdapter
import work.racka.reluct.common.database.dao.DatabaseWrapper

internal actual fun getDatabaseWrapper(): DatabaseWrapper {
    val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        .also { ReluctDatabase.Schema.create(it) }
    return DatabaseWrapper(ReluctDatabase(driver, GoalsTableAdapter = GoalsTableAdapter))
}