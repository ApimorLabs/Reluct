package work.racka.thinkrchive.v2.common.integration.di

import org.koin.core.module.Module

internal expect object Platform {
    fun platformIntegrationModule(): Module
}