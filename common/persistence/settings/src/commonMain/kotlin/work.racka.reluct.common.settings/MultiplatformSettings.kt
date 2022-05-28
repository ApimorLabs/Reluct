package work.racka.reluct.common.settings

import kotlinx.coroutines.flow.Flow

interface MultiplatformSettings {
    val theme: Flow<Int>
    fun saveThemeSettings(value: Int): Boolean
}