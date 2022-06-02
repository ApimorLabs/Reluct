package work.racka.reluct.common.features.screen_time.di

import org.koin.core.module.Module

internal expect object Platform {
    fun installModule(): Module
}