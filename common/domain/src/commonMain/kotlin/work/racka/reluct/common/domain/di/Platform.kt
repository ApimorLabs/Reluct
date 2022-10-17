package work.racka.reluct.common.domain.di

import org.koin.core.module.Module

internal expect object Platform {
    fun installModule(): Module
}
