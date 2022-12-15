package work.racka.reluct.common.network.di

import org.koin.core.module.Module

expect object Platform {
    fun module(): Module
}
