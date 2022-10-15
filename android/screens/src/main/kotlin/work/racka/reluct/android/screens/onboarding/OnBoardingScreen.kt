package work.racka.reluct.android.screens.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.Dispatchers
import work.racka.common.mvvm.koin.compose.getCommonViewModel
import work.racka.reluct.common.features.onboarding.vm.OnBoardingViewModel

@Composable
fun OnBoardingScreen(
    navigateHome: () -> Unit
) {
    val viewModel: OnBoardingViewModel = getCommonViewModel()
    val uiState by viewModel.uiState.collectAsState(Dispatchers.Main.immediate)

    OnBoardingUI(
        uiState = uiState,
        updateCurrentPage = viewModel::updateCurrentPage,
        updatePermission = viewModel::updatePermission,
        saveTheme = viewModel::saveTheme,
        onToggleAppBlocking = viewModel::toggleAppBlocking,
        onBoardingComplete = {
            viewModel.onBoardingComplete()
            navigateHome()
        }
    )
}