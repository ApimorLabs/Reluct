package work.racka.reluct.common.settings.repository

import kotlinx.coroutines.flow.SharedFlow

interface MultiplatformSettings {
    val theme: SharedFlow<Int>
    fun saveThemeSettings(value: Int)
}