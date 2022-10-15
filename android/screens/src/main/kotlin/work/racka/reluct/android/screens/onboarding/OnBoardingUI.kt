package work.racka.reluct.android.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.onboarding.components.OnBoardingBottomBar
import work.racka.reluct.android.screens.onboarding.pages.*
import work.racka.reluct.common.features.onboarding.states.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.OnBoardingState
import work.racka.reluct.common.features.onboarding.states.PermissionType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun OnBoardingUI(
    modifier: Modifier = Modifier,
    uiState: OnBoardingState,
    updateCurrentPage: (OnBoardingPages) -> Unit,
    updatePermission: (permissionType: PermissionType, isGranted: Boolean) -> Unit,
    saveTheme: (themeValue: Int) -> Unit,
    onToggleAppBlocking: (isEnabled: Boolean) -> Unit,
    onBoardingComplete: () -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            OnBoardingBottomBar(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = Dimens.MediumPadding.size),
                uiState = uiState,
                onUpdatePage = updateCurrentPage,
                onCompleted = onBoardingComplete
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = uiState.currentPage,
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
                        isGranted = uiState.permissionsState.notificationGranted,
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
                        isGranted = uiState.permissionsState.alarmsAndRemindersGranted,
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
                        goBack = { updateCurrentPage(OnBoardingPages.Reminders) },
                        isGranted = uiState.permissionsState.usageAccessGranted,
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
                        isGranted = uiState.permissionsState.overlayGranted,
                        isAppBlockingEnabled = uiState.appBlockingEnabled,
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
                        selectedTheme = uiState.currentThemeValue,
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