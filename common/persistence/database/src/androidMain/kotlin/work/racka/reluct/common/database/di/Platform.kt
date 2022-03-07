package work.racka.thinkrchive.v2.common.database.di

import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.dsl.module
import work.racka.thinkrchive.v2.common.database.dao.ThinkrchiveDatabaseWrapper
import work.racka.thinkrchive.v2.common.database.db.ThinkpadDatabase
import work.racka.thinkrchive.v2.common.database.util.Constants

internal actual object Platform {
    actual fun platformDatabaseModule() = module {
        single {
            val driver = AndroidSqliteDriver(
                schema = ThinkpadDatabase.Schema,
                context = get(),
                name = Constants.THINKPAD_DATABASE
            )
            ThinkrchiveDatabaseWrapper(ThinkpadDatabase(driver))
        }
    }
}
