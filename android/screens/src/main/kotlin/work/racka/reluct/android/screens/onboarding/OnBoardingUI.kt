package work.racka.reluct.android.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import work.racka.reluct.android.compose.theme.Dimens
import work.racka.reluct.android.screens.onboarding.components.OnBoardingBottomBar
import work.racka.reluct.android.screens.onboarding.pages.*
import work.racka.reluct.common.features.onboarding.states.OnBoardingPages
import work.racka.reluct.common.features.onboarding.states.OnBoardingState
import work.racka.reluct.common.features.onboarding.states.PermissionType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingUI(
    modifier: Modifier = Modifier,
    uiState: OnBoardingState,
    updateCurrentPage: (OnBoardingPages) -> Unit,
    updatePermission: (permissionType: PermissionType, isGranted: Boolean) -> Unit,
    saveTheme: (themeValue: Int) -> Unit,
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
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = Dimens.MediumPadding.size)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState.currentPage) {
                is OnBoardingPages.Welcome -> {
                    WelcomePage()
                }
                is OnBoardingPages.Permissions -> {
                    PermissionsPage()
                }
                is OnBoardingPages.Notifications -> {
                    NotificationsPage(
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
                    UsageAccessPage(
                        isGranted = uiState.permissionsState.overlayGranted,
                        updatePermissionCheck = { isGranted ->
                            updatePermission(
                                PermissionType.OVERLAY,
                                isGranted
                            )
                        }
                    )
                }
                else -> {
                    Text(text = "Others")
                }
            }
        }
    }
}