package work.racka.reluct.android.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.screens.onboarding.components.OnBoardingBottomBar
import work.racka.reluct.android.screens.onboarding.pages.*
import work.racka.reluct.android.screens.util.isAndroid13Plus
import work.racka.reluct.common.features.onboarding.states.onBoarding.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.onBoarding.OnBoardingState
import work.racka.reluct.common.features.onboarding.states.onBoarding.PermissionType
import work.racka.reluct.compose.common.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoardingUI(
    uiState: State<OnBoardingState>,
    updateCurrentPage: (OnBoardingPages) -> Unit,
    updatePermission: (permissionType: PermissionType, isGranted: Boolean) -> Unit,
    saveTheme: (themeValue: Int) -> Unit,
    onToggleAppBlocking: (isEnabled: Boolean) -> Unit,
    onBoardingComplete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            OnBoardingBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = Dimens.MediumPadding.size),
                uiStateProvider = { uiState.value },
                onUpdatePage = updateCurrentPage,
                onCompleted = onBoardingComplete
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.value.currentPage,
            modifier = Modifier
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { targetState ->
            when (targetState) {
                is OnBoardingPages.Welcome -> {
                    WelcomePage()
                }
                is OnBoardingPages.Permissions -> {
                    PermissionsPage(goBack = { updateCurrentPage(OnBoardingPages.Welcome) })
                }
                is OnBoardingPages.Notifications -> {
                    NotificationsPage(
                        goBack = { updateCurrentPage(OnBoardingPages.Permissions) },
                        isGranted = uiState.value.permissionsState.notificationGranted,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.NOTIFICATION,
                                isGranted
                            )
                        }
                    )
                }
                is OnBoardingPages.Reminders -> {
                    AlarmsAndRemindersPage(
                        goBack = { updateCurrentPage(OnBoardingPages.Notifications) },
                        isGranted = uiState.value.permissionsState.alarmsAndRemindersGranted,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.REMINDERS,
                                isGranted
                            )
                        }
                    )
                }
                is OnBoardingPages.UsageAccess -> {
                    UsageAccessPage(
                        goBack = {
                            if (isAndroid13Plus()) {
                                updateCurrentPage(OnBoardingPages.Reminders)
                            } else {
                                updateCurrentPage(OnBoardingPages.Permissions)
                            }
                        },
                        isGranted = uiState.value.permissionsState.usageAccessGranted,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.USAGE_ACCESS,
                                isGranted
                            )
                        }
                    )
                }
                is OnBoardingPages.Overlay -> {
                    OverlayPage(
                        goBack = { updateCurrentPage(OnBoardingPages.UsageAccess) },
                        isGranted = uiState.value.permissionsState.overlayGranted,
                        isAppBlockingEnabled = uiState.value.appBlockingEnabled,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.OVERLAY,
                                isGranted
                            )
                        },
                        onToggleAppBlocking = onToggleAppBlocking
                    )
                }
                is OnBoardingPages.Themes -> {
                    ThemesPage(
                        selectedTheme = uiState.value.currentThemeValue,
                        onSelectTheme = saveTheme,
                        goBack = { updateCurrentPage(OnBoardingPages.Overlay) }
                    )
                }
                is OnBoardingPages.AllSet -> {
                    AllSetPage(goBack = { updateCurrentPage(OnBoardingPages.Themes) })
                }
            }
        }
    }
}
