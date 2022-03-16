package work.racka.reluct.common.features.tasks.di

import org.koin.core.module.Module

internal expect object Platform {
    fun platformTasksModule(): Module
}
