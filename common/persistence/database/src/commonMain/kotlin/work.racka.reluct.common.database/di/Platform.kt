package work.racka.thinkrchive.v2.common.database.di

import org.koin.core.module.Module

internal expect object Platform {
    fun platformDatabaseModule(): Module
}
