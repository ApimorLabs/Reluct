package work.racka.reluct.common.features.settings.di

import org.koin.core.module.Module

internal expect object Platform {
    fun module(): Module
}