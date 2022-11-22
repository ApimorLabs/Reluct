package racka.reluct.common.authentication.di

import org.koin.core.module.Module

internal expect object Platform {
    fun installModule(): Module
}
