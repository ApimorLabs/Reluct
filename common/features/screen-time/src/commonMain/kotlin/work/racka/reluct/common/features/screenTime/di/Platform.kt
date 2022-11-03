package work.racka.reluct.common.features.screenTime.di

import org.koin.core.module.Module

internal expect object Platform {
    fun installModule(): Module
}
