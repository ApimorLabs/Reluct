package work.racka.reluct.common.settings.di

import org.koin.core.module.Module

internal expect object Platform {
    fun platformSettingsModule(): Module
}
