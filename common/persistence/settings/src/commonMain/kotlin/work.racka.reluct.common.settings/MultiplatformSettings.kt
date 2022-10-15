package work.racka.reluct.common.settings

import kotlinx.coroutines.flow.Flow

interface MultiplatformSettings {
    fun saveThemeSettings(value: Int): Boolean
    val theme: Flow<Int>
    fun saveFocusMode(value: Boolean): Boolean
    val focusMode: Flow<Boolean>
    fun saveDoNotDisturb(value: Boolean): Boolean
    val doNoDisturb: Flow<Boolean>
    fun saveVersionCode(value: Int): Boolean
    val savedVersionCode: Flow<Int>
    fun saveOnBoardingShown(value: Boolean): Boolean
    val onBoardingShown: Flow<Boolean>
    fun saveAppBlocking(value: Boolean): Boolean
    val appBlockingEnabled: Flow<Boolean>
}