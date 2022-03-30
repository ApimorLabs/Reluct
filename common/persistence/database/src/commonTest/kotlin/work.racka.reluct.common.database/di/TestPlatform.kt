package work.racka.reluct.common.database.di

import org.koin.core.module.Module

internal expect object TestPlatform {
    fun platformDatabaseModule(): Module
}
