package work.racka.reluct.android.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.common.features.onboarding.vm.OnBoardingViewModel

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun OnBoardingScreen(
    onNavigateToAuth: () -> Unit
) {
    val viewModel: OnBoardingViewModel = getCommonViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    OnBoardingUI(
        uiState = uiState,
        updateCurrentPage = viewModel::updateCurrentPage,
        updatePermission = viewModel::updatePermission,
        saveTheme = viewModel::saveTheme,
        onToggleAppBlocking = viewModel::toggleAppBlocking,
        onBoardingComplete = {
            viewModel.onBoardingComplete()
            onNavigateToAuth()
        }
    )
}
