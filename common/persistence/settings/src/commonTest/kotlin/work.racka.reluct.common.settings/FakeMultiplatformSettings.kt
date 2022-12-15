package work.racka.reluct.common.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

class FakeMultiplatformSettings : MultiplatformSettings {

    private val themeFlow = MutableStateFlow(Keys.Defaults.THEME)
    private val focusModeFlow = MutableStateFlow(false)
    private val doNotDisturbFlow = MutableStateFlow(false)
    private val savedVersionCodeFlow = MutableStateFlow(1)
    private val onBoardingShownFlow = MutableStateFlow(true)
    private val appBlockingEnabledFlow = MutableStateFlow(true)
    private val loginSkippedFlow = MutableStateFlow(true)

    override fun saveThemeSettings(value: Int): Boolean {
        themeFlow.update { value }
        return true
    }

    override val theme: Flow<Int> = themeFlow.asSharedFlow()

    override fun saveFocusMode(value: Boolean): Boolean {
        focusModeFlow.update { value }
        return true
    }

    override val focusMode: Flow<Boolean> = focusModeFlow.asSharedFlow()

    override fun saveDoNotDisturb(value: Boolean): Boolean {
        doNotDisturbFlow.update { value }
        return true
    }

    override val doNoDisturb: Flow<Boolean> = doNotDisturbFlow.asSharedFlow()

    override fun saveVersionCode(value: Int): Boolean {
        savedVersionCodeFlow.update { value }
        return true
    }

    override val savedVersionCode: Flow<Int> = savedVersionCodeFlow.asSharedFlow()

    override fun saveOnBoardingShown(value: Boolean): Boolean {
        onBoardingShownFlow.update { value }
        return true
    }

    override val onBoardingShown: Flow<Boolean> = onBoardingShownFlow.asSharedFlow()

    override fun saveAppBlocking(value: Boolean): Boolean {
        appBlockingEnabledFlow.update { value }
        return true
    }

    override val appBlockingEnabled: Flow<Boolean> = appBlockingEnabledFlow.asSharedFlow()

    override fun saveLoginSkipped(value: Boolean): Boolean {
        loginSkippedFlow.update { value }
        return true
    }

    override val loginSkipped: Flow<Boolean> = loginSkippedFlow.asSharedFlow()
}
