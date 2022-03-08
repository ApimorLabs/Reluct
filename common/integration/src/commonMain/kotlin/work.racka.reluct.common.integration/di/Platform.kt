package work.racka.reluct.common.integration.di

import org.koin.core.module.Module

internal expect object Platform {
    fun platformIntegrationModule(): Module
}