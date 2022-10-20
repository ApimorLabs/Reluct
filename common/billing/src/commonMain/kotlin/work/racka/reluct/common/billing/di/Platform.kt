package work.racka.reluct.common.billing.di

import org.koin.core.module.Module

internal expect object Platform {
    fun module(): Module
}