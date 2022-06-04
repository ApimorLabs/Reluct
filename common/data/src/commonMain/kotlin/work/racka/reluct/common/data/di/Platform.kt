package work.racka.reluct.common.data.di

import org.koin.core.module.Module

internal expect object Platform {
    fun installModule(): Module
}
