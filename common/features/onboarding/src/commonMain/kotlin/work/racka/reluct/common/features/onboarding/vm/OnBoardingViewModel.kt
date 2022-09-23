package work.racka.reluct.common.features.onboarding.vm

import kotlinx.coroutines.flow.*
import work.racka.common.mvvm.viewmodel.CommonViewModel
import work.racka.reluct.common.features.onboarding.states.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.OnBoardingState
import work.racka.reluct.common.features.onboarding.states.PermissionType
import work.racka.reluct.common.features.onboarding.states.PermissionsState
import work.racka.reluct.common.settings.MultiplatformSettings

class OnBoardingViewModel(
    private val settings: MultiplatformSettings
) : CommonViewModel() {

    private val currentPage: MutableStateFlow<OnBoardingPages> =
        MutableStateFlow(OnBoardingPages.Welcome)
    private val permissionsState = MutableStateFlow(PermissionsState())

    val uiState: StateFlow<OnBoardingState> = combine(
        currentPage, permissionsState, settings.theme
    ) { currentPage, permissionsState, currentThemeValue ->
        OnBoardingState(
            currentPage = currentPage,
            permissionsState = permissionsState,
            currentThemeValue = currentThemeValue
        )
    }.stateIn(
        scope = vmScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = OnBoardingState()
    )

    fun updateCurrentPage(page: OnBoardingPages) {
        currentPage.update { page }
    }

    fun updatePermission(permissionType: PermissionType, isGranted: Boolean) {
        when (permissionType) {
            PermissionType.NOTIFICATION -> {
                permissionsState.update { it.copy(notificationGranted = isGranted) }
            }
            PermissionType.USAGE_ACCESS -> {
                permissionsState.update { it.copy(usageAccessGranted = isGranted) }
            }
            PermissionType.REMINDERS -> {
                permissionsState.update { it.copy(alarmsAndRemindersGranted = isGranted) }
            }
            PermissionType.OVERLAY -> {
                permissionsState.update { it.copy(overlayGranted = isGranted) }
            }
        }
    }

    fun saveTheme(themeValue: Int) {
        settings.saveThemeSettings(themeValue)
    }

    fun onBoardingComplete() {
        settings.saveOnBoardingShown(true)
    }

    // TODO: Add Authentication Methods and States
}