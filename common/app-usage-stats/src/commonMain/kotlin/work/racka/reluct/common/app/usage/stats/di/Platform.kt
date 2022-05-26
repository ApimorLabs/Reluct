package work.racka.reluct.common.app.usage.stats.di

import org.koin.core.module.Module

internal expect object Platform {
    fun module(): Module
}
