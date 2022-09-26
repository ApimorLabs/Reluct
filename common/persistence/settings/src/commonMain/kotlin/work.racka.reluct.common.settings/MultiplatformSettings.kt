package work.racka.reluct.common.settings

import kotlinx.coroutines.flow.Flow

interface MultiplatformSettings {
    val theme: Flow<Int>
    fun saveThemeSettings(value: Int): Boolean
    val focusMode: Flow<Boolean>
    fun saveFocusMode(value: Boolean): Boolean
    val doNoDisturb: Flow<Boolean>
    fun saveDoNotDisturb(value: Boolean): Boolean
    val savedVersionCode: Flow<Int>
    fun saveVersionCode(value: Int): Boolean
    val onBoardingShown: Flow<Boolean>
    fun saveOnBoardingShown(value: Boolean): Boolean
}